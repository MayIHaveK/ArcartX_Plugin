/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.event

import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.Event

/**
 * ArcartX 事件基类，替代 TabooLib BukkitProxyEvent
 * 每个子类必须自己声明 companion object { @JvmStatic val handlerList = HandlerList() }
 * 并 override fun getHandlers() = handlerList
 */
abstract class ArcartXEvent(
    val allowCancelled: Boolean = true
) : Event(), Cancellable {

    private var isCancelled = false

    override fun isCancelled() = isCancelled

    override fun setCancelled(cancel: Boolean) {
        if (allowCancelled) {
            isCancelled = cancel
        }
    }

    fun call(): Boolean {
        Bukkit.getPluginManager().callEvent(this)
        return !isCancelled
    }
}
