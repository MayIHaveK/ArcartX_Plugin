/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.ui

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.config.ui.type.UI
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.core.ui.adapter.ArcartXUI
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.arcartx.util.collections.CallBack
import java.io.File
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

/** UI 注册表 */
object ArcartXUIRegistry {

    private val _registeredUI: MutableMap<String, UI> = ConcurrentHashMap()

    val registeredUI: Map<String, UI> = Collections.unmodifiableMap(_registeredUI)

    fun register(id: String, src: YamlConfiguration): ArcartXUI = register(id, UI(id, src))

    fun register(id: String, src: String): ArcartXUI =
        register(id, UI(id, YamlConfiguration.loadConfiguration(src.reader())))

    fun register(id: String, src: File): ArcartXUI =
        register(id, UI(id, YamlConfiguration.loadConfiguration(src)))

    fun register(id: String, ui: UI): ArcartXUI {
        _registeredUI[id] = ui
        return ui
    }

    fun unregister(id: String) {
        _registeredUI.remove(id)
    }

    operator fun get(id: String): ArcartXUI? {
        return registeredUI[id]
    }

    fun reload(id: String, src: YamlConfiguration) = reload(id, UI(id, src))

    fun reload(id: String, src: String) =
        reload(id, UI(id, YamlConfiguration.loadConfiguration(src.reader())))

    fun reload(id: String, src: File) =
        reload(id, UI(id, YamlConfiguration.loadConfiguration(src)))

    private fun reload(id: String, ui: UI) {
        _registeredUI[id]?.let { ui.callbacks.putAll(it.callbacks) }
        _registeredUI[id] = ui
        ArcartXEntityManager.players.values.forEach { NetworkMessageSender.sendUI(it.player, ui) }
    }

    fun open(player: Player, id: String) {
        registeredUI[id]?.open(player)
    }

    fun open(player: Player, id: String, callBack: CallBack) {
        registeredUI[id]?.open(player,callBack)
    }

    fun close(player: Player, id: String) {
        registeredUI[id]?.close(player)
    }

    fun sendPacket(player: Player, id: String, handlerName: String, packet: Any) {
        registeredUI[id]?.sendPacket(player, handlerName, packet)
    }

    fun run(player: Player, id: String, code: String) {
        registeredUI[id]?.run(player, code)
    }

    fun openUnsafe(player: Player, id: String) {
        NetworkMessageSender.sendOpenUI(player, id)
    }

    fun openUnsafe(player: Player, id: String, callBack: CallBack) {
        ArcartXEntityManager.getPlayer(player)!!.callbacks["OpenCallBack:$id"] = callBack
        NetworkMessageSender.sendOpenUI(player, id)
    }

    fun closeUnsafe(player: Player, id: String) {
        NetworkMessageSender.sendCloseUI( player, id)
    }

    fun sendPacketUnsafe(player: Player, id: String, handlerName: String, packet: Any) {
        NetworkMessageSender.sendScreenPacket(player, handlerName, packet, id)
    }

    fun runUnsafe(player: Player, id: String, code: String) {
        NetworkMessageSender.sendUIRunCode(player, id, code)
    }


    fun Player.openUI(uiHandler: UIHandler){
        uiHandler.open(this)
    }

    fun Player.closeUI(uiHandler: UIHandler){
        uiHandler.close(this)
    }

}