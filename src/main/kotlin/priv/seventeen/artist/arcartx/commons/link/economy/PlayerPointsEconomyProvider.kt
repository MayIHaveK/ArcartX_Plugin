/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.economy

import org.black_ixx.playerpoints.PlayerPoints
import org.bukkit.entity.Player

/** PlayerPoints 经济系统实现 */
class PlayerPointsEconomyProvider: EconomyProvider {


    override fun getIdentifier(): String {
        return "PlayerPoints"
    }


    override fun addEconomy(player: Player, amount: Double): Boolean {
        return PlayerPoints.getInstance().api.give(player.uniqueId, amount.toInt());
    }

    override fun getEconomy(player: Player): Double {
        return PlayerPoints.getInstance().api.look(player.uniqueId).toDouble();
    }

    override fun takeEconomy(player: Player, amount: Double): Boolean {
        if(PlayerPoints.getInstance().api.look(player.uniqueId) >= amount.toInt()) {
            return PlayerPoints.getInstance().api.take(player.uniqueId, amount.toInt());
        }
        return false;
    }


}