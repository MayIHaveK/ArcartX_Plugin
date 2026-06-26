/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.controller

import org.bukkit.configuration.file.YamlConfiguration
import priv.seventeen.artist.arcartx.core.config.animation.Controller
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/** 控制器注册表 */
object ControllerRegistry {

    val controllers = ConcurrentHashMap<String, Controller>()

    fun register(id: String, src: YamlConfiguration) {
        controllers[id] = Controller(src)
    }

    fun register(id: String, src: String) {
        val yaml = YamlConfiguration.loadConfiguration(src.reader())
        controllers[id] = Controller(yaml)
    }

    fun register(id: String, src: File) {
        val yaml = YamlConfiguration.loadConfiguration(src)
        controllers[id] = Controller(yaml)
    }

    fun unregister(id: String) {
        controllers.remove(id)
    }

    fun syncPlayers(){
        ArcartXEntityManager.players.values.parallelStream().forEach {
            NetworkMessageSender.sendController(it.player)
        }
    }

}