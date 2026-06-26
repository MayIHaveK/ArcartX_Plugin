/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.modellink

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class ModelLinkFolder : ArcartXConfigFolder<ModelLinkSetting>(bukkitPlugin,"model_link", ::ModelLinkSetting) {

    val elements: MutableMap<String, ModelLinkData> = HashMap()

    init {
        load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,   "${folderPath}模型链接示例.yml")
    }


    override fun reload() {
        super.reload()
        elements.clear()
        this.configs.values.forEach {
            it.elements.forEach { (_, v) ->
                elements[v.targetModelID] = v
            }
        }

        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_MODEL_LINK, elements.size.toString()))
    }

}