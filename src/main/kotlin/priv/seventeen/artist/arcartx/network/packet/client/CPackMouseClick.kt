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
import priv.seventeen.artist.arcartx.event.client.ClientMouseClickEvent

/** 鼠标点击包 */
class CPackMouseClick : ClientPacket {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("action")
    var action: Int = 0

    override fun handle(player: Player) {
        ClientMouseClickEvent(player,id,action).call()
    }

    override val isAsync: Boolean
        get() = false
}