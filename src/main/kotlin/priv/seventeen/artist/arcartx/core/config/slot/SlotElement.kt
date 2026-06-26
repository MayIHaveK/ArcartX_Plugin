/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.slot

import com.google.gson.annotations.SerializedName
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit
import priv.seventeen.artist.arcartx.script.ScriptArgs

class SlotElement() : ArcartXConfigSection() {

    @SerializedName("id")
    @Implicit
    var id = "Slot1"

    @SerializedName("attribute")
    var attribute: String = "none"
        private set

    @SerializedName("loadSubstitutionModel")
    var loadSubstitutionModel: Boolean = false

    @SerializedName("limit")
    var limit = ArrayList<String>()
        private set

    @SerializedName("update")
    var update = ArrayList<String>()
        private set

    var limitScriptArgs: Set<Pair<String,ScriptArgs>>? = null
        private set

    var updateScriptArgs: Set<Pair<String,ScriptArgs>>? = null
        private set


    override fun load(plugin: JavaPlugin, filePath: String, configurationSection: ConfigurationSection?) {
        super.load(plugin,filePath,configurationSection)
        limitScriptArgs = limit.map { ScriptArgs.parseConfig(it) }.toSet()
        updateScriptArgs = update.map { ScriptArgs.parseConfig(it) }.toSet()
    }


}