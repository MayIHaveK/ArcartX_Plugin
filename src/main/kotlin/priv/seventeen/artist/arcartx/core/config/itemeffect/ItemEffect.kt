/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.itemeffect

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin


class ItemEffect(plugin: JavaPlugin = bukkitPlugin, pathName: String) : ArcartXConfig(plugin, pathName) {



    @SerializedName("root")
    var data: MutableMap<String, ItemEffectData> = LinkedHashMap()
        private set

    init {
        this.load()
    }

}


class ItemEffectData : ArcartXConfigSection() {


    @SerializedName("matchKey")
    var matchKey: String = ""
        private set

    @SerializedName("matchValue")
    var matchValue: String = ""

    @SerializedName("before")
    var before = false

    @SerializedName("attribute")
    var attribute:  MutableMap<String, String> = HashMap()
}


