/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.font

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class FontIconFolder: ArcartXConfigFolder<FontIconFolder.FontConfig>(bukkitPlugin, "font_icon", ::FontConfig) {

    val fontIconIds: MutableMap<String, FontIcon> = HashMap()

    init {
        load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,  "${folderPath}文字图标示例.yml")
    }


    override fun reload() {
        super.reload()
        fontIconIds.clear()

        this.configs.values.forEach{ v ->
            v.fontSetting.forEach { (key, value) ->
                fontIconIds[key] = value
            }
        }

        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_FONT_ICON, fontIconIds.size.toString()))
    }


    class FontConfig(plugin: JavaPlugin, pathName: String) :
        ArcartXConfig(plugin, pathName) {

        @SerializedName(value = "root")
        var fontSetting: MutableMap<String, FontIcon> = hashMapOf("example" to FontIcon())
            private set


        init {
            this.load()
        }
    }
}