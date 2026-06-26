/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.client

import com.google.gson.annotations.SerializedName
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.ui.ArcartXUIRegistry
import priv.seventeen.artist.arcartx.core.ui.adapter.CallBackType
import priv.seventeen.artist.arcartx.event.client.ClientCustomPacketEvent
import priv.seventeen.artist.arcartx.util.collections.CallData

/** 自定义数据包，支持 UI 回调和通用事件 */
class CPackCustomPacket : ClientPacket {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("data")
    var data: List<String>? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("name")
    var name: String? = null

    override fun handle(player: Player) {
        val packetId = id ?: return
        val packetData = data ?: emptyList()

        if (type == "ui" && name != null) {
            val arcartXUI = ArcartXUIRegistry.registeredUI[name]
            if (arcartXUI != null && arcartXUI.callbacks.containsKey(CallBackType.PACKET)) {
                arcartXUI.callbacks[CallBackType.PACKET]?.forEach {
                    it.call(CallData(player, packetId, packetData))
                }
            } else {
                ClientCustomPacketEvent(player, packetId, packetData).call()
            }
        } else {
            ClientCustomPacketEvent(player, packetId, packetData).call()
        }
    }

    override val isAsync: Boolean
        get() = false
}