/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.listener

import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.event.client.ClientEntityJoinEvent
import priv.seventeen.artist.blink.event.AutoListener

@AutoListener
fun onEntityJoinPlayerWorld(event : ClientEntityJoinEvent){
    ArcartXEntityManager.entities[event.entityUUID]?.startSeenBy(event.player)
    ArcartXEntityManager.players[event.entityUUID]?.startSeenBy(event.player)

}
