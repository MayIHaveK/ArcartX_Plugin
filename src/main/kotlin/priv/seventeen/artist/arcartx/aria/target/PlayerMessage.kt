/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.aria.target

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.ui.ArcartXUIRegistry
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** 玩家消息 */
object PlayerMessage {

    /**
     * 显示标题
     * 参数: 主标题, 副标题, 淡入时间, 停留时间, 淡出时间
     */
    @JvmStatic
    @AriaInvokeHandler(value = "showTitle", target = Player::class)
    fun showTitle(data: InvocationData) {
        if (data.size() != 5) return
        val title = data[0].stringValue()
        val subtitle = data[1].stringValue()
        val fadeIn = data[2].intValue()
        val stay = data[3].intValue()
        val fadeOut = data[4].intValue()
        val player = data.target as Player
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
    }

    /**
     * 发送消息
     * 参数: 消息
     */
    @JvmStatic
    @AriaInvokeHandler(value = "sendMessage", target = Player::class)
    fun sendMessage(data: InvocationData) {
        if (data.size() != 1) return
        val message = data[0].stringValue()
        val player = data.target as Player
        player.sendMessage(message)
    }

    @JvmStatic
    @AriaInvokeHandler(value = "sendUIPacket", target = Player::class)
    fun sendUIPacket(data: InvocationData) {
        if (data.size() != 3) return
        ArcartXUIRegistry.sendPacket(data.target as Player, data.get(0).stringValue(), data.get(1).stringValue(), data.get(2).stringValue())
    }
}
