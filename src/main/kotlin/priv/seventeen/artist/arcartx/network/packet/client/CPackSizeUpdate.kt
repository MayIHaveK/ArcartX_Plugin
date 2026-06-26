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
import java.util.*

/** 实体尺寸同步包 */
class CPackSizeUpdate : ClientPacket {

    @SerializedName("target")
    private val target: UUID? = null

    override fun handle(player: Player) {
        val targetUUID = target ?: return
        ArcartXEntityManager.getEntity(targetUUID)?.syncSize()
    }

    override val isAsync: Boolean
        get() = false
}
