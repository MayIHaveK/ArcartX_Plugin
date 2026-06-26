/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.key.simple

import com.google.gson.annotations.SerializedName
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.script.ScriptArgs
import priv.seventeen.artist.arcartx.util.collections.KeyCallBack

class SimpleKey (plugin: JavaPlugin, path: String) : ArcartXConfig(plugin, path){

    @SerializedName("root")
    var simpleKeyElements: MutableMap<String, SimpleKeyElement> = HashMap()
        private set

    init {
        this.load()
    }

}

class SimpleKeyElement() : ArcartXConfigSection() {

    @SerializedName("keys")
    var keys: MutableList<String> = ArrayList()
        private set


    @SerializedName("trigger")
    @Transient
    private var then: MutableList<String> = ArrayList()

    @Transient
    var callBack: KeyCallBack? = null

    @Transient
    var triggerScript: Set<Pair<String,ScriptArgs>>? = null


    constructor(keys: MutableList<String>, then: MutableList<String>) : this() {
        this.keys = keys
        this.then = then
    }

    override fun load(plugin: JavaPlugin, filePath: String, configurationSection: ConfigurationSection?) {
        super.load(plugin, filePath, configurationSection)
        triggerScript = then.map { ScriptArgs.parseConfig(it) }.toSet()
    }



}