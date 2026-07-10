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
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.arcartx.core.config.animation.Controller
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.blink.bukkitPlugin
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 控制器注册表。
 */
object ControllerRegistry {

    val controllers = ConcurrentHashMap<String, Controller>()

    fun register(id: String, src: YamlConfiguration) {
        val controller = try {
            Controller(src)
        } catch (e: Exception) {
            bukkitPlugin.sendException("客户端控制器 $id 解析失败，已拒绝注册: ${e.message}",e)
            return
        }
        controllers[id] = controller
    }

    fun register(id: String, src: String) {
        register(id, YamlConfiguration.loadConfiguration(src.reader()))
    }

    fun register(id: String, src: File) {
        register(id, YamlConfiguration.loadConfiguration(src))
    }

    fun unregister(id: String) {
        controllers.remove(id)
    }

    fun getSubControllerIds(controllerId: String): Set<String>? =
        controllers[controllerId]?.controllers?.keys

    fun getStateIds(controllerId: String, subControllerId: String): Set<String>? =
        controllers[controllerId]?.controllers?.get(subControllerId)?.states?.keys

    fun hasController(id: String): Boolean = controllers.containsKey(id)

    fun syncPlayers(){
        ArcartXEntityManager.players.values.forEach {
            NetworkMessageSender.sendController(it.player)
        }
    }

}
