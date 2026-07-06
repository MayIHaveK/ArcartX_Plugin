/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.trmenu

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.blink.bukkitPlugin

/**
 * TrMenu 容器点击兼容桥。
 */
object TrMenuBridge {

    private const val PLUGIN_NAME = "TrMenu"

    val isAvailable: Boolean by lazy { Bukkit.getPluginManager().getPlugin(PLUGIN_NAME) != null }


    fun routeClick(player: Player, slot: Int, click: ClickType, action: InventoryAction? = null): Boolean {
        if (!isAvailable) return false
        return try {
            TrMenuApi.route(player, slot, click, action)
        } catch (t: Throwable) {
            bukkitPlugin.sendException("TrMenuBridge", t as? Exception ?: RuntimeException(t))
            // 已确认 TrMenu 存在；即便异常也视为已消费，避免回退逻辑再对 TrMenu 菜单派发多余的 InventoryClickEvent
            true
        }
    }
}
