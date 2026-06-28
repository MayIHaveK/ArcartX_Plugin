/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network

import com.google.gson.annotations.SerializedName
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.arcartx.network.encryptor.Base64Encryptor
import priv.seventeen.artist.arcartx.network.message.DecodeType
import priv.seventeen.artist.arcartx.network.message.MessageID
import priv.seventeen.artist.arcartx.network.packet.client.ClientPacket
import priv.seventeen.artist.arcartx.network.packet.client.*
import priv.seventeen.artist.arcartx.util.ByteArrayUtils
import priv.seventeen.artist.arcartx.util.JsonUtils.fromJson
import priv.seventeen.artist.blink.bukkitPlugin
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

/** 插件消息通道管理器，负责客户端数据包的接收与分发 */
class NetworkManager : PluginMessageListener {

    companion object{
        const val CHANNEL = "arcartx:main"

        /** 压缩标志位：消息首字节为该值时表示负载经过压缩 */
        const val COMPRESSED_FLAG = 32

        // —— 分片重组的防滥用上限，防止恶意客户端用畸形/永不补齐的分片耗尽内存 ——
        private const val MAX_FRAGMENT_COUNT = 1024                  // size（总分片数）合法上限
        private const val MAX_PART_MESSAGE_LENGTH = 4 * 1024 * 1024  // 单条消息累积字符上限
        private const val MAX_PART_ENTRIES = 512                     // 在途半包条目总上限
        private const val PART_MESSAGE_EXPIRE_MS = 30_000L           // 未补齐分片过期时间
    }


    private val packets = HashMap<Int, Class<out ClientPacket?>>()

    init {
        Bukkit.getMessenger().registerIncomingPluginChannel(bukkitPlugin, CHANNEL, this)
        Bukkit.getMessenger().registerOutgoingPluginChannel(bukkitPlugin, CHANNEL)
        bukkitPlugin.sendMessage(L(AXLanguageKey.REGISTER_CHANNEL, CHANNEL))

        MessageID.checkDuplicates()
        registerPacket(MessageID.Client.CONNECTION, CPackConnection::class.java)
        registerPacket(MessageID.Client.KEY_GROUP_PRESS, CPackKeyGroupPress::class.java)
        registerPacket(MessageID.Client.SIMPLE_KEY_PRESS, CPackSimpleKeyPress::class.java)
        registerPacket(MessageID.Client.CUSTOM, CPackCustomPacket::class.java)
        registerPacket(MessageID.Client.SCREEN_DO, CPackDoWithScreen::class.java)
        registerPacket(MessageID.Client.ENTITY_JOIN, CPackEntityJoin::class.java)
        registerPacket(MessageID.Client.PAPI, CPackRequestPlaceholder::class.java)
        registerPacket(MessageID.Client.INITIALIZE, CPackInitialized::class.java)
        registerPacket(MessageID.Client.CLICK_SLOT, CPackClickSlot::class.java)
        registerPacket(MessageID.Client.GET_SLOT, CPackRequestSlot::class.java)
        registerPacket(MessageID.Client.SIZE_DATA, CPackSizeUpdate::class.java)
        registerPacket(MessageID.Client.BONE_HIT, CPackHitBone::class.java)
        registerPacket(MessageID.Client.KEY_PRESS, CPackClientKey::class.java)
        registerPacket(MessageID.Client.DONE,CPackResourceLoaded::class.java)
        registerPacket(MessageID.Client.BLOCK_MODEL, CPackEntityBlockJoin::class.java)
        registerPacket(MessageID.Client.MOUSE_CLICK, CPackMouseClick::class.java)
        registerPacket(MessageID.Client.UI_DATA, CPackUIData::class.java)
        registerPacket(MessageID.Client.MOVE_BREAK, CPackMoveBreak::class.java)
        registerPacket(MessageID.Client.CONTAINER_CLICK, CPackContainerClick::class.java)
    }

    fun close(){
        Bukkit.getMessenger().unregisterIncomingPluginChannel(bukkitPlugin)
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(bukkitPlugin)
    }

    private fun registerPacket(enumMessage: MessageID, packetBase: Class<out ClientPacket?>) {
        this.packets[enumMessage.id] = packetBase
    }


    private val partMessage: MutableMap<String, PartialMessage> = ConcurrentHashMap()

    /** 在途分片缓存条目：累积内容 + 最后更新时间（用于超时清理） */
    private class PartialMessage {
        val builder = StringBuilder()
        var lastUpdate = System.currentTimeMillis()
    }

    fun clearPartMessages(playerUUID: String) {
        partMessage.keys.removeAll { it.startsWith(playerUUID) }
    }

    /** 清理超时未补齐的分片缓存，防止恶意客户端用永不补齐的半包累积内存 */
    private fun purgeExpiredParts() {
        val now = System.currentTimeMillis()
        partMessage.entries.removeAll { now - it.value.lastUpdate > PART_MESSAGE_EXPIRE_MS }
    }

    override fun onPluginMessageReceived(channel: String, player: Player, bytes: ByteArray) {
        if (!channel.equals(CHANNEL, ignoreCase = true)) {
            return
        }

        var buf = Unpooled.wrappedBuffer(bytes)
        var jsonSrc = ""
        try {
            if (buf.readByte().toInt() == COMPRESSED_FLAG) {
                buf = Unpooled.wrappedBuffer(ByteArrayUtils.decompress(readBytes(buf)))
                jsonSrc = buf.toString(StandardCharsets.UTF_8)
                buf.release()
            }
        } catch (e: Exception) {
            // 畸形/超限的压缩包直接丢弃，避免解压炸弹及异常逃逸到插件消息线程
            return
        }
        if (jsonSrc.isEmpty()) {
            return
        }

        val message: BaseMessage = jsonSrc.fromJson( BaseMessage::class.java)

        val id: Int = message.id
        val level: Int = message.type
        val code: Int = message.code
        var msg: String? = message.message
        val part: Int = message.part
        val size: Int = message.size

        // 校验分片字段合法性，拒绝畸形/超大声明（part 必须落在 [0, size) 内）
        if (size < 1 || size > MAX_FRAGMENT_COUNT || part < 0 || part >= size) {
            return
        }

        val partKey = player.uniqueId.toString() + id
        if (size - part != 1) {
            purgeExpiredParts()
            // 在途半包条目过多时不再为新 key 开缓存，防止被大量不同 id 的半包撑爆内存
            if (partMessage.size >= MAX_PART_ENTRIES && !partMessage.containsKey(partKey)) {
                return
            }
            val partial = partMessage.getOrPut(partKey) { PartialMessage() }
            if (partial.builder.length + (msg?.length ?: 0) > MAX_PART_MESSAGE_LENGTH) {
                partMessage.remove(partKey)   // 累积超限，丢弃整条
                return
            }
            partial.builder.append(msg)
            partial.lastUpdate = System.currentTimeMillis()
            return
        } else {
            if (size > 1) {
                val partial = partMessage.remove(partKey)
                msg = (partial?.builder?.toString() ?: "") + msg
            }
        }

        try {
            if (level == DecodeType.AES.id) {
                msg = ArcartXEntityManager.getPlayer(player)?.encryptor?.decode(msg!!)
            } else {
                msg = Base64Encryptor.base64Decrypt(msg)
            }
            if (packets[code] == null) {
                return
            }
            msg?.fromJson(packets[code] as Class<out ClientPacket?>)?.run(player)
        } catch (e: Exception) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_PACKET_HANDLE), e)
        }
    }



    private fun readBytes(buf: ByteBuf): ByteArray {
        val bytes = ByteArray(buf.readableBytes())
        val readerIndex = buf.readerIndex()
        buf.getBytes(readerIndex, bytes)
        return bytes
    }


}

class BaseMessage {
    @SerializedName("id")
    var id = 0

    @SerializedName("code")
    var code = 0

    @SerializedName("type")
    var type = 0

    @SerializedName("size")
    var size = 1

    @SerializedName("part")
    var part = 0

    @SerializedName("message")
    var message: String = ""
}