/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.ui

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.api.ArcartXAPI.getUIRegistry
import priv.seventeen.artist.arcartx.core.ui.adapter.ArcartXUI
import priv.seventeen.artist.arcartx.core.ui.adapter.CallBackType
import java.io.File

/** UI 处理器基类 */
abstract class UIHandler(val identifier: String, private val uiConfig: File) {

    abstract val plugin: JavaPlugin

    val ui: ArcartXUI = getUIRegistry().register(this.identifier, this.uiConfig).apply {
        registerCallBack(CallBackType.OPEN){
            onOpenPost(it.player)
        }
        registerCallBack(CallBackType.CLOSE){
            onClose(it.player)
        }
        registerCallBack(CallBackType.PACKET){
            onPacket(it.player, it.identifier, it.data)
        }
    }

    open fun open(player: Player) {
        onOpenPre(player)
        ui.open(player)
    }

    open fun close(player: Player) {
        ui.close(player)
    }

    open fun onOpenPre(player: Player) {

    }


    open fun onOpenPost(player: Player) {

    }

    open fun onClose(player: Player) {

    }

    open fun onPacket(player: Player, identifier: String, data: List<String>){

    }

    fun sendPacket(player: Player, packetHandler: String, packet: Any){
        ui.sendPacket(player, packetHandler, packet)
    }

    fun sendRun(player: Player, shimmerCode: String){
        ui.run(player, shimmerCode)
    }

}