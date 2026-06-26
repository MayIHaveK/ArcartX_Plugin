/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.hologram.data

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Comment
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit

@Comment("全息数据")
@Comment("该全息作为生物血条时，可可获取以下变量")
@Comment("val.name:生物名称")
@Comment("val.health:生物血量")
@Comment("val.maxHealth:生物最大血量")
class Hologram(plugin: JavaPlugin, pathName: String) : ArcartXConfig(plugin, pathName) {


    @SerializedName("id")
    @Implicit
    var id: String = "unknown"

    @SerializedName("width")
    var width = 10
        private set

    @SerializedName("height")
    var height = 10
        private set

    @SerializedName("action")
    var actions: Map<String, String> = HashMap()
        private set

    @SerializedName("element")
    var elements = LinkedHashMap<String, HologramElement>()
        private set

    init {
        this.load()
    }


}


class HologramElement : ArcartXConfigSection() {

    @SerializedName("type")
    var type = "none"
        private set

    @SerializedName("attribute")
    var attributes: Map<String, Any> = HashMap()
        private set

}