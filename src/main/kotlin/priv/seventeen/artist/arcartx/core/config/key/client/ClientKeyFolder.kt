/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.key.client

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.core.keybind.ArcartXKeyBindRegistry
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin


class ClientKeyFolder: ArcartXConfigFolder<ClientKey>(bukkitPlugin,  "key_bind/client_key", ::ClientKey) {

    val elementMap: MutableMap<String, ClientKeyElement> = LinkedHashMap()

    init {
        this.load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,   "${folderPath}客户端按键示例.yml")
    }


    override fun reload() {
        super.reload()
        elementMap.clear()
        this.configs.values.forEach { elementMap.putAll(it.clientKeyElementMap) }

        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_CLIENT_KEY, elementMap.size.toString()))

        elementMap.putAll(ArcartXKeyBindRegistry.clientKeyBind)

    }
}