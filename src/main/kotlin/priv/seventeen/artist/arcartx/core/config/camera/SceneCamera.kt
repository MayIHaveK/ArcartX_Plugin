/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.camera

import com.google.gson.annotations.SerializedName
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

class SceneCamera : ArcartXConfig {

    @SerializedName("mode")
    var mode = 0

    @SerializedName("backTime")
    var backTime = 1000
        private set


    @SerializedName("step")
    var elements: MutableMap<String, SceneElement> = LinkedHashMap()
        private set


    constructor(plugin: JavaPlugin, pathName: String) : super(plugin, pathName){
        this.load()
    }

    constructor(name : String) : super(bukkitPlugin, "camera/scene/$name")
}

class SceneElement() : ArcartXConfigSection() {

    @SerializedName("x")
    var x = 0.0
        private set

    @SerializedName("y")
    var y = 0.0
        private set

    @SerializedName("z")
    var z = 0.0
        private set

    @SerializedName("yaw")
    var yaw = 0f
        private set

    @SerializedName("pitch")
    var pitch = 0f
        private set

    @SerializedName("bezier")
    var bezier = Bezier()
        private set

    @SerializedName("transition")
    var transition = 800
        private set

    @SerializedName("keep")
    var keep = 800
        private set

    constructor(location: Location) : this() {
        this.x = String.format("%.2f", location.x).toDouble()
        this.y = String.format("%.2f", location.y).toDouble()
        this.z = String.format("%.2f", location.z).toDouble()
        this.yaw = String.format("%.2f", location.yaw.toDouble()).toFloat()
        this.pitch = String.format("%.2f", location.pitch.toDouble()).toFloat()
    }
}