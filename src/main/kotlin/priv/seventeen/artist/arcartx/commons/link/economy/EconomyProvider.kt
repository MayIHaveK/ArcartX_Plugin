/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.economy

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX

/** 经济系统提供者接口 */
interface EconomyProvider {

    fun getIdentifier(): String

    fun getDisplayName(): String {
        return ArcartX.configs.economySetting.root[getIdentifier()] ?: getIdentifier();
    }

    fun addEconomy(player: Player, amount: Double): Boolean

    fun getEconomy(player: Player): Double

    fun takeEconomy(player: Player,  amount: Double): Boolean

}