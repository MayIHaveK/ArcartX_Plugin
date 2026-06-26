/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.client

import com.google.gson.annotations.SerializedName
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.core.ui.ArcartXUIRegistry
import priv.seventeen.artist.arcartx.core.ui.adapter.ArcartXUI
import priv.seventeen.artist.arcartx.core.ui.adapter.CallBackType
import priv.seventeen.artist.arcartx.event.client.ClientHudCloseEvent
import priv.seventeen.artist.arcartx.event.client.ClientHudOpenEvent
import priv.seventeen.artist.arcartx.event.client.ClientMenuCloseEvent
import priv.seventeen.artist.arcartx.event.client.ClientMenuOpenEvent
import priv.seventeen.artist.arcartx.util.collections.CallData

/** 界面操作包，处理 HUD/菜单的打开和关闭 */
class CPackDoWithScreen : ClientPacket {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("cmd")
    var cmd: String? = null

    @SerializedName("type")
    var type: String? = null

    override fun handle(player: Player) {
        val screenName = name ?: return
        val arcartXUI: ArcartXUI = ArcartXUIRegistry[screenName] ?: return
        if(cmd.equals("open")) {
            if(type == "menu") ClientMenuOpenEvent(player,arcartXUI).call() else ClientHudOpenEvent(player,arcartXUI).call()
            ArcartXEntityManager.getPlayer(player)?.let {
                it.callbacks.remove("OpenCallBack:${arcartXUI.id}")?.call()
            }
            arcartXUI.callbacks[CallBackType.OPEN]?.forEach {
                it.call(CallData(player,"", listOf()))
            }
        } else {
            if(type == "menu") ClientMenuCloseEvent(player,arcartXUI).call() else ClientHudCloseEvent(player,arcartXUI).call()
            arcartXUI.callbacks[CallBackType.CLOSE]?.forEach { it.call(CallData(player,"", listOf())) }
        }
    }

    override val isAsync: Boolean
        get() = false
}