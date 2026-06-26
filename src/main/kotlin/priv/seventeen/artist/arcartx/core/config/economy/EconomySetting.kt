/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.economy

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.annotation.Comment
import priv.seventeen.artist.blink.bukkitPlugin

@Comment("配置货币展示名(重启生效)")
class EconomySetting(plugin: JavaPlugin = bukkitPlugin, pathName: String = "link/economy.yml") : ArcartXConfig(plugin, pathName) {

    @SerializedName("root")
    var root: Map<String, String> = HashMap()

    init {
        this.load()
    }

}