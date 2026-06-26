/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.hologram.entity

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection

class HologramEntity(plugin: JavaPlugin, pathName: String) : ArcartXConfig(plugin,pathName) {

    @SerializedName("root")
    var elements: MutableMap<String, HologramEntityElement> = HashMap()
        private set

    init {
        this.load()
    }

}

class HologramEntityElement : ArcartXConfigSection() {

    @SerializedName("match")
    var match: List<String> = ArrayList()
        private set

    @SerializedName("keepTime")
    var keepTime: Long = 2000
        private set

    // 淡出/淡入时间
    @SerializedName("fadeTime")
    var fadeTime: Long = 500
        private set

    @SerializedName("hologram")
    var hologram = ""
        private set
}