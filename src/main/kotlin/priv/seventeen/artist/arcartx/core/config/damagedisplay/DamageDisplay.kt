/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.damagedisplay

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

class DamageDisplay(plugin: JavaPlugin = bukkitPlugin, pathName: String) : ArcartXConfig(plugin, pathName) {

    @SerializedName("root")
    var elements: MutableMap<String, DamageDisplayData> = HashMap()
        private set


    init {
        this.load()
    }

}


class DamageDisplayData : ArcartXConfigSection() {


    @SerializedName("textureWidth")
    var textureWidth = 0.0

    @SerializedName("textureHeight")
    var textureHeight = 0.0

    @SerializedName("textures")
    var textures = ArrayList<String>()

    // 随机值最大值
    @SerializedName("randomMax")
    var randomMax: Double = 0.0

    // 随机值最小值
    @SerializedName("randomMin")
    var randomMin: Double = 0.0

    // 显示小数位数
    @SerializedName("decimalPlaces")
    var decimalPlaces: Int = 0


}