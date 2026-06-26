/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.event.plugin

import org.bukkit.event.HandlerList
import priv.seventeen.artist.arcartx.event.ArcartXEvent

/** 插件重载事件 */
class ArcartXReloadEvent() : ArcartXEvent(allowCancelled = false) {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers() = handlerList
}
