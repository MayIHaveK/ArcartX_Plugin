/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.waypoint

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

class Waypoint(plugin: JavaPlugin = bukkitPlugin, pathName: String) : ArcartXConfig(plugin, pathName) {

    @SerializedName("root")
    var elements: MutableMap<String, WaypointData> = HashMap()
        private set


    init {
        this.load()
    }

}


class WaypointData : ArcartXConfigSection() {

    @SerializedName("normal")
    var normalTexture = ""

    @SerializedName("left")
    var left = ""

    @SerializedName("right")
    var right = ""

    @SerializedName("width")
    var width = 20.0

    @SerializedName("height")
    var height = 20.0

    @SerializedName("font")
    var font = ""

    @SerializedName("fontSize")
    var fontSize = 16

    @SerializedName("icon")
    var icon: String = ""

}