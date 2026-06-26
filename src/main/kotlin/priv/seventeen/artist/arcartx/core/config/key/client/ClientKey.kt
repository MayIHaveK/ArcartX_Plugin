/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.key.client

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit
import priv.seventeen.artist.arcartx.util.collections.KeyCallBack

class ClientKey(plugin: JavaPlugin, path: String): ArcartXConfig(plugin,path) {

    @SerializedName("root")
    var clientKeyElementMap: LinkedHashMap<String, ClientKeyElement> = LinkedHashMap()
        private set

    init {
        this.load()
        clientKeyElementMap.forEach { (key, element) ->
            element.id = key
        }
    }

}

class ClientKeyElement(): ArcartXConfigSection(){

    @SerializedName("id")
    @Implicit
    var id = ""

    @SerializedName("category")
    var category = "ArcartX 自定义按键"

    @SerializedName("default")
    var defaultKey = "M"

    @Transient
    var callBack: KeyCallBack ? = null


    constructor (id: String, category: String, defaultKey: String) : this() {
        this.id = id
        this.category = category
        this.defaultKey = defaultKey
    }
}