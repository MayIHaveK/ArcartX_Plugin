/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.blink.bukkitPlugin

/** MythicMobs 关键词配置 */
class KeywordConfig(plugin: JavaPlugin = bukkitPlugin, path: String = "link/mythicmobs_key_words.yml") : ArcartXConfig(plugin,path){

    @SerializedName("model")
    var model: String = "model"

    @SerializedName("animation")
    var animation: String = "animation"

    @SerializedName("hitbox")
    var hitbox: String = "hitbox"

    @SerializedName("defaultState")
    var defaultState: String = "defaultState"

    @SerializedName("hideBone")
    var hideBone: String = "hideBone"

    @SerializedName("sound3d")
    var sound: String = "sound3d"

    @SerializedName("bedrockParticle")
    var bedrockParticle: String = "bedrockParticle"

    @SerializedName("hammerEffect")
    var hammerEffect: String = "hammerEffect"

    @SerializedName("uiPacket")
    var uiPack: String = "uiPacket"

    @SerializedName("hitboxProxy")
    var hitboxProxy: String = "hitboxProxy"

    @SerializedName("cameraShake")
    var cameraShake: String = "cameraShake"

    @SerializedName("proxyMountType")
    var proxyMountType: String = "proxyMountType"

    @SerializedName("proxyMountSpeed")
    var proxyMountSpeed: String = "proxyMountSpeed"

    @SerializedName("proxyAddSeat")
    var proxyAddSeat: String = "proxyAddSeat"

    init {
        this.load()
    }

}