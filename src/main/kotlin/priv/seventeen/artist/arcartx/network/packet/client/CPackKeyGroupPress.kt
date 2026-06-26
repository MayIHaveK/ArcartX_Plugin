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
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.event.client.ClientKeyGroupPressEvent
import priv.seventeen.artist.arcartx.script.ScriptManager

/** 按键组触发包 */
class CPackKeyGroupPress : ClientPacket {

    @SerializedName("group")
    var group: String? = null


    override fun handle(player: Player) {
        val groupName = group ?: return
        ClientKeyGroupPressEvent(player, groupName).call()

        ArcartX.configs.keyGroupFolder.elementMap[groupName]?.let {
            it.triggerScript?.forEach{ script ->
                ScriptManager.executeScript(script.first,player,script.second)
            }
        }
    }

    override val isAsync: Boolean
        get() = false


}