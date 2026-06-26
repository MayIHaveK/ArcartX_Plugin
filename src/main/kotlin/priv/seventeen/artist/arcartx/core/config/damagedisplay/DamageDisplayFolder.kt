/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.damagedisplay

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class DamageDisplayFolder : ArcartXConfigFolder<DamageDisplay>(bukkitPlugin,"damage_display", ::DamageDisplay) {

    val elements: MutableMap<String, DamageDisplayData> = HashMap()

    init {
        load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,   "${folderPath}伤害显示示例.yml")
    }


    override fun reload() {
        super.reload()
        elements.clear()
        this.configs.values.forEach {
            it.elements.forEach { (k, v) ->
                elements[k] = v
            }
        }

        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_DAMAGE_DISPLAY, elements.size.toString()))
    }

}