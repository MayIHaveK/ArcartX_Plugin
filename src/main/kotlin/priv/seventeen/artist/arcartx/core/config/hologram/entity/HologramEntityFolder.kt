/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.hologram.entity

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class HologramEntityFolder: ArcartXConfigFolder<HologramEntity>(bukkitPlugin, "hologram/entity_heal_bar", ::HologramEntity) {

    val elements: MutableMap<String, HologramEntityElement> = HashMap()

    init {
        this.load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,   "${folderPath}血条示例.yml")
    }


    override fun reload() {
        super.reload()
        elements.clear()
        this.configs.values.forEach { elements.putAll(it.elements) }
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_ENTITY_HOLOGRAM, elements.size.toString()))
    }
}