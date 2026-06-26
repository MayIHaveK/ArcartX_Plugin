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
import priv.seventeen.artist.arcartx.event.client.ClientEntityJoinEvent
import priv.seventeen.artist.arcartx.event.client.ClientInitializedEvent
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import java.util.*

/** 客户端初始化完成包 */
class CPackInitialized : ClientPacket {

    @SerializedName("entity")
    var entities: HashSet<UUID> = HashSet()

    @SerializedName("reload")
    var reload: Boolean = false

    override fun handle(player: Player) {
        if(reload){
            ClientInitializedEvent.Reload(player).call()
        } else {
            NetworkMessageSender.sendWorldChange(player, player.world)
            entities.forEach {
                ClientEntityJoinEvent(player, it).call()
            }
            ArcartXEntityManager.getPlayer(player)?.syncSlotCacheToClient()
            ClientInitializedEvent.End(player).call()
        }
    }


    override val isAsync: Boolean
        get() = false
}
