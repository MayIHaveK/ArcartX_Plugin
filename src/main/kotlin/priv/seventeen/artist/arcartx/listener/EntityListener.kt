/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.listener

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.blink.event.AutoListener


@AutoListener
fun onEntityDelete(event: EntityDeathEvent) {
    if (event.entity !is Player) {
        ArcartXEntityManager.removeEntity(event.entity)
    }
}
