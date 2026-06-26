/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.hologram.world

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection

class HologramLocation(plugin: JavaPlugin, pathName: String) : ArcartXConfig(plugin,pathName) {

    @SerializedName("root")
    var elements: MutableMap<String, HologramLocationElement> = HashMap()
        private set

    init {
        this.load()
    }


}

class HologramLocationElement : ArcartXConfigSection(){

    @SerializedName("world")
    var world = "world"
        private set

    @SerializedName("x")
    var x = 1.0
        private set

    @SerializedName("y")
    var y = 1.0
        private set

    @SerializedName("z")
    var z = 1.0
        private set

    @SerializedName("yaw")
    var yaw = 50f
        private set

    @SerializedName("pitch")
    var pitch = 90f
        private set

    @SerializedName("follow")
    var follow = false
        private set

    @SerializedName("hologram")
    var hologram = "全息示例"
        private set

}