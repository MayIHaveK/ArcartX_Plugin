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
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.lifecycle.Awake
import priv.seventeen.artist.blink.lifecycle.LifeCycle

/** TrMenu 集成：容器点击兼容层 */
object TrMenuHooker {

    @Awake(LifeCycle.ACTIVE)
    fun load() {
        if (Bukkit.getPluginManager().getPlugin("TrMenu") == null) return
        if (TrMenuBridge.isAvailable) {
            bukkitPlugin.sendMessage("§a已发现 §bTrMenu§a，容器点击兼容层已启用")
        } else {
            bukkitPlugin.sendMessage("§e已发现 §bTrMenu§e，但容器点击兼容层接入失败")
        }
    }
}
