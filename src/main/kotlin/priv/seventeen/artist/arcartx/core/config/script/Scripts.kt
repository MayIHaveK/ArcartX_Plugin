/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.script

import com.google.gson.annotations.SerializedName
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

class Scripts(pathName: String) : ArcartXConfig(bukkitPlugin,pathName) {

    @SerializedName("root")
    var scripts: HashMap<String, ScriptElement> = HashMap()


    init {
        this.load()
    }
}

class ScriptElement : ArcartXConfigSection(){

    @SerializedName("type")
    var type: String = "normal"

    @SerializedName("target")
    var target: String = ""

    @SerializedName("script")
    var script: String = ""


}