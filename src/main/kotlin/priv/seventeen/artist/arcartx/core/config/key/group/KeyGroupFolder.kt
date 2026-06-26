/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.key.group

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class KeyGroupFolder: ArcartXConfigFolder<KeyGroup>(bukkitPlugin, "key_bind/key_group", ::KeyGroup) {

    val elementMap: MutableMap<String, KeyGroupElement> = HashMap()

    init {
        this.load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,   "${folderPath}按键组示例.yml")
    }


    override fun reload() {
        super.reload()
        elementMap.clear()
        this.configs.values.forEach {
            it.keyGroupElementMap.forEach{(id, element) ->
                element.id = id
                elementMap[id] = element
            }
        }
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_KEY_GROUP, elementMap.size.toString()))
    }

}