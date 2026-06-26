/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.model

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

class EntityModel(plugin: JavaPlugin = bukkitPlugin, pathName: String) : ArcartXConfig(plugin,pathName) {

    @SerializedName("root")
    var elements: MutableMap<String, EntityModelData> = HashMap()
        private set

    init {
        this.load()
    }

}

class EntityModelData : ArcartXConfigSection() {

    @SerializedName("matchName")
    var matchName: String = ""
        private set

    @SerializedName("modelID")
    var modelID: String = ""
        private set


    @SerializedName("scale")
    var scale: Double = 1.0
        private set

    @SerializedName("hideNameTag")
    var hideNameTag: Boolean = false

    @SerializedName("virtualWidth")
    var width: Double = -1.0

    @SerializedName("virtualHeight")
    var height: Double = -1.0

    @SerializedName("renderAlways")
    var renderAlways = false

    @SerializedName("hurtColor")
    var hurtColor: Boolean = true

}

