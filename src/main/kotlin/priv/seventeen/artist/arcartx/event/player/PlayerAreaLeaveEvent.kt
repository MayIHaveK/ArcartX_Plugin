/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.event.player

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import priv.seventeen.artist.arcartx.core.config.area.Area
import priv.seventeen.artist.arcartx.event.ArcartXEvent

/** 玩家离开区域事件 */
class PlayerAreaLeaveEvent(val player : Player , val area : Area, val newArea: Area?) : ArcartXEvent(allowCancelled = false) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers() = handlerList
}
