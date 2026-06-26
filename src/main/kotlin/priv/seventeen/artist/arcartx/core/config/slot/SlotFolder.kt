/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.slot

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class SlotFolder: ArcartXConfigFolder<SlotConfig>(bukkitPlugin,"extra_slot", ::SlotConfig) {

    val setting: MutableMap<String, SlotElement> = LinkedHashMap()

    init {
        this.load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,  "${folderPath}额外槽位示例.yml")
    }


    override fun reload() {
        super.reload()
        setting.clear()
        this.configs.values.forEach {
            it.slots.forEach{(id, element) ->
                element.id = id
                setting[id] = element
            }
        }
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_SLOT, setting.size.toString()))
    }

}

class SlotConfig(plugin: JavaPlugin, pathName: String) : ArcartXConfig(plugin, pathName) {

    @SerializedName("root")
    var slots: MutableMap<String, SlotElement> = LinkedHashMap()
        private set

    init {
        this.load()
    }
}