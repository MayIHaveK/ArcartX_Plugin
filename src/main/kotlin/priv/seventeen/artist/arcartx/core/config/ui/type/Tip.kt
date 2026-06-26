/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.ui.type

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.core.config.ui.control.Control
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit

class Tip(formPlugin: JavaPlugin, pathName: String) :ArcartXConfig(formPlugin, pathName) {

    @SerializedName("id")
    @Implicit
    lateinit var id: String

    @SerializedName("tip")
    var tipData = TipData()
        private set

    @SerializedName("root_control")
    var controls: Control = Control()
        private set


    init {
        this.load()
    }
}

class TipData : ArcartXConfigSection() {

    @SerializedName("match")
    var match = ArrayList<String>()
        private set



}