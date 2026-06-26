/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.event.client

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import priv.seventeen.artist.arcartx.event.ArcartXEvent

/** 鼠标点击事件 */
class ClientMouseClickEvent(
    val player: Player,
    val button: Int,
    val action: Int
) : ArcartXEvent(allowCancelled = false) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers() = handlerList
}
