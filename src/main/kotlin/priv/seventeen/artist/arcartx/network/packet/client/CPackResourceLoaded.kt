/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.client

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.event.client.ClientInitializedEvent

/** 资源加载完成包 */
class CPackResourceLoaded : ClientPacket {


    override fun handle(player: Player) {
        ClientInitializedEvent.ResourceLoaded(player).call()
    }


    override val isAsync: Boolean
        get() = false
}