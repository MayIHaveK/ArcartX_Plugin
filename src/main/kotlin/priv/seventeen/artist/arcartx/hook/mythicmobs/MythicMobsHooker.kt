/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs

import org.bukkit.Bukkit
import priv.seventeen.artist.arcartx.hook.mythicmobs.bullet.ModelBullet
import priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox.ArcartXHitboxSupport
import priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox.WrappedHitboxSupport
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.lifecycle.Awake
import priv.seventeen.artist.blink.lifecycle.LifeCycle

/** MythicMobs 集成入口 */
object MythicMobsHooker {

    var config: KeywordConfig? = null


    @Awake(LifeCycle.LOAD)
    fun registerBullet() {
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            ModelBullet.register()
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun load() {
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            config = KeywordConfig()
            bukkitPlugin.sendMessage(L(AXLanguageKey.FOUND_MYTHICMOBS))
            Bukkit.getPluginManager().registerEvents(MythicMobsListener(), bukkitPlugin)
            if(Bukkit.getPluginManager().getPlugin("ModelEngine") != null){
                bukkitPlugin.sendMessage(L(AXLanguageKey.FOUND_MODEL_ENGINE))
                WrappedHitboxSupport.setup()
            } else {
                ArcartXHitboxSupport.setup()
            }
        }
    }

}