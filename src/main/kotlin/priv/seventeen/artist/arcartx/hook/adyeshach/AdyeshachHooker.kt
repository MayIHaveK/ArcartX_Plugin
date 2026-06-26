/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.adyeshach

import org.bukkit.Bukkit
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.command.AdyeshachCommand
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.lifecycle.Awake
import priv.seventeen.artist.blink.lifecycle.LifeCycle

/** Adyeshach NPC 框架集成 */
object AdyeshachHooker {

    @Awake(LifeCycle.ACTIVE)
    fun load() {
        if (Bukkit.getPluginManager().getPlugin("Adyeshach") != null) {
            Bukkit.getPluginManager().registerEvents(AdyeshachListener(), bukkitPlugin)
            ArcartX.axCommand.registerSubCommand(AdyeshachCommand())
            bukkitPlugin.sendMessage(L(AXLanguageKey.FOUND_ADYESHACH))
        }
    }
}
