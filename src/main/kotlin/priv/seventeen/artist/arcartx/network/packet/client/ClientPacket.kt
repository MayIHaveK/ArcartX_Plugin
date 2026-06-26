/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.client

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler.runAsync
import priv.seventeen.artist.blink.bukkitPlugin

/** 客户端数据包接口，定义客户端→服务端包的处理与调度方式 */
interface ClientPacket {

    fun handle(player: Player)

    val isAsync: Boolean
        get() = true

    fun run(player: Player) {
        if (isAsync) {
            bukkitPlugin.runAsync { handle(player) }
        } else {
            handle(player)
        }
    }

}
