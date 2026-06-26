/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.ui.adapter

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.arcartx.util.collections.CallBack
import priv.seventeen.artist.arcartx.util.collections.UICallBack

interface ArcartXUI {

    val callbacks : MutableMap<CallBackType, MutableList<UICallBack>>

    var id : String

    fun registerCallBack(type: CallBackType, callBack: UICallBack) {
        if (!callbacks.containsKey(type)) {
            callbacks[type] = ArrayList()
        }
        callbacks[type]!!.add(callBack)
    }


    fun open(player: Player){
        NetworkMessageSender.sendOpenUI(player, this.id)
    }

    fun open(player: Player, callBack: CallBack){
        ArcartXEntityManager.getPlayer(player)!!.callbacks["OpenCallBack:$id"] = callBack
        NetworkMessageSender.sendOpenUI(player, this.id)
    }


    fun close(player: Player) {
        NetworkMessageSender.sendCloseUI( player, this.id)
    }


    fun sendPacket(player: Player, handlerName: String, packet: Any){
        NetworkMessageSender.sendScreenPacket(player, handlerName, packet, this.id)
    }

    fun run(player: Player, code: String) {
        NetworkMessageSender.sendUIRunCode(player, this.id, code)
    }


}