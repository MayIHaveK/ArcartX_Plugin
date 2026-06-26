/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.key.simple

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.core.keybind.ArcartXKeyBindRegistry
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class SimpleKeyFolder: ArcartXConfigFolder<SimpleKey>(bukkitPlugin, "key_bind/simple_key", ::SimpleKey) {

    val element: MutableMap<String, SimpleKeyElement> = HashMap()

    init {
        this.load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,  "${folderPath}简单按键示例.yml")
    }


    override fun reload() {
        super.reload()
        element.clear()
        this.configs.values.forEach { element.putAll(it.simpleKeyElements) }
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_SIMPLE_KEY, element.size.toString()))

        element.putAll(ArcartXKeyBindRegistry.simpleKeyBind)
    }
}