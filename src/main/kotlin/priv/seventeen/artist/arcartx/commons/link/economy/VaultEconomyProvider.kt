/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.economy

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.entity.Player
import priv.seventeen.artist.blink.bukkitPlugin

/** Vault 经济系统实现 */
class VaultEconomyProvider: EconomyProvider {

    override fun getIdentifier(): String {
        return "Vault"
    }

    private val economy: Economy? = bukkitPlugin.server.servicesManager.getRegistration(Economy::class.java)?.provider


    override fun addEconomy(player: Player, amount: Double): Boolean {
        return economy?.depositPlayer(player, amount)?.type == EconomyResponse.ResponseType.SUCCESS
    }

    override fun getEconomy(player: Player): Double {
        return economy?.getBalance(player)?: 0.0
    }

    override fun takeEconomy(player: Player, amount: Double): Boolean {
        if(economy?.has(player, amount) == true) {
            return economy.withdrawPlayer(player, amount).type == EconomyResponse.ResponseType.SUCCESS
        }
        return false
    }


}