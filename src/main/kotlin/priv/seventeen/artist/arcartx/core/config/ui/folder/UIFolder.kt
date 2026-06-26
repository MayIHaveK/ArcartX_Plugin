/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.ui.folder

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.core.config.ui.type.UI
import priv.seventeen.artist.arcartx.core.ui.ArcartXUIRegistry
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class UIFolder : ArcartXConfigFolder<UI>(bukkitPlugin, "ui", ::UI) {

    init {
        this.load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,  "${folderPath}使用该功能请一定先看文档.yml")
    }

    override fun onSetFileID(config: UI, id: String) {
        config.id = id
    }


    override fun reload() {
        // 注销注册表里的UI
        val keys = ArcartXUIRegistry.registeredUI.keys.toMutableList()
        keys.forEach {
            if(this.configs.keys.contains(it)){
                ArcartXUIRegistry.unregister(it)
            }
        }
        super.reload()

        this.configs.forEach { (k, v) ->
            ArcartXUIRegistry.register(k, v)
        }

        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_UI, configs.size.toString()))
    }
}