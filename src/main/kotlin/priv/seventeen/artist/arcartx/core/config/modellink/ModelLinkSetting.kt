/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.modellink

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

class ModelLinkSetting(plugin: JavaPlugin = bukkitPlugin, pathName: String) : ArcartXConfig(plugin,pathName) {

    @SerializedName("root")
    var elements: MutableMap<String, ModelLinkData> = HashMap()
        private set

    init {
        this.load()
    }

}

class ModelLinkData : ArcartXConfigSection() {

    @SerializedName("targetModelID")
    var targetModelID: String = ""
        private set

    @SerializedName("import")
    var import: List<String> = ArrayList()
        private set



}