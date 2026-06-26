/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.area

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class AreaFolder : ArcartXConfigFolder<Area>(bukkitPlugin,"area", ::Area) {

    init {
        load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
    }

    override fun onSetFileID(config: Area, id: String) {
        config.name = id
    }

    override fun reload() {
        super.reload()
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_AREA, configs.size.toString()))
    }
}