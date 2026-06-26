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
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

class CameraPreset : ArcartXConfig {

    @SerializedName("root")
    var elements : MutableMap<String, CameraElement> = HashMap()
        private set

    constructor(plugin: JavaPlugin, pathName: String) :super(plugin, pathName){
        this.load()
    }

    constructor(name : String) : super(bukkitPlugin, "camera/preset/$name")

}

class Bezier : ArcartXConfigSection() {

    @SerializedName("x1")
    var x1 = 0.1
        private set

    @SerializedName("y1")
    var y1 = 0.25
        private set

    @SerializedName("x2")
    var x2 = 0.1
        private set


    @SerializedName("y2")
    var y2 = 0.25
        private set


}


class CameraElement : ArcartXConfigSection() {

    @SerializedName("offsetX")
    var offsetX = -3.0

    @SerializedName("offsetY")
    var offsetY = 0.5

    @SerializedName("offsetZ")
    var offsetZ = 3.0

    // 开启自由相机
    @SerializedName("enableFree")
    var enableFree = false

    @SerializedName("hideHead")
    var hideHead = false

    @SerializedName("bezier")
    var bezier = Bezier()

    @SerializedName("transition")
    var transition = 1000

    @SerializedName("modelCamera")
    var modelCamera = ""
}