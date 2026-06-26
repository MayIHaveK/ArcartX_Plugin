/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.key.group

import com.google.gson.annotations.SerializedName
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit
import priv.seventeen.artist.arcartx.script.ScriptArgs

class KeyGroup(plugin: JavaPlugin, path: String) : ArcartXConfig(plugin, path){

    @SerializedName("root")
    var keyGroupElementMap: MutableMap<String, KeyGroupElement> = HashMap()
        private set

    init {
        this.load()
    }

}

class KeyGroupElement() : ArcartXConfigSection() {

    @SerializedName("id")
    @Implicit
    var id = "unknown"


    @SerializedName("keys")
    var keys: MutableList<String> = ArrayList()
        private set


    @SerializedName("trigger")
    @Transient
    private var then = ArrayList<String>()

    @SerializedName("interval")
    var interval = 100
        private set

    @Transient
    var triggerScript: Set<Pair<String, ScriptArgs>>? = null

    override fun load(plugin: JavaPlugin, filePath: String, configurationSection: ConfigurationSection?) {
        super.load(plugin, filePath, configurationSection)
        triggerScript = then.map { ScriptArgs.parseConfig(it) }.toSet()
    }
}