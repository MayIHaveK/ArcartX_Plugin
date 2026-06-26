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
import priv.seventeen.artist.arcartx.event.client.ClientExtraSlotRequestEvent

/** 额外槽位请求包 */
class CPackRequestSlot : ClientPacket {
    @SerializedName("slotName")
    var slotName: String? = null


    override fun handle(player: Player) {
        val slot = slotName ?: return
        ClientExtraSlotRequestEvent(player, slot).call()
    }

    override val isAsync: Boolean
        get() = false
}