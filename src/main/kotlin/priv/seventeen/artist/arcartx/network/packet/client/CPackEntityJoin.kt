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
import priv.seventeen.artist.arcartx.event.client.ClientEntityJoinEvent
import priv.seventeen.artist.arcartx.event.client.ClientEntityLeaveEvent
import java.util.*

/** 实体进入/离开客户端视野包 */
class CPackEntityJoin() : ClientPacket {

    @SerializedName("UUID")
    var uuid: UUID? = null

    @SerializedName("isJoin")
    var isJoin: Boolean = false

    override fun handle(player: Player) {
        val entityUUID = uuid ?: return
        if (isJoin) {
            ClientEntityJoinEvent(player, entityUUID).call()
        } else {
            ClientEntityLeaveEvent(player, entityUUID).call()
        }
    }

    override val isAsync: Boolean
        get() = false
}