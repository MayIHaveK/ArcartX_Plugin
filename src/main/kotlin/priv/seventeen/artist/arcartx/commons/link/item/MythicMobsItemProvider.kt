/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.item

import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.inventory.ItemStack

/** MythicMobs 物品提供者 */
class MythicMobsItemProvider : ItemProvider {

    override fun getIdentifier(): String {
        return "MythicMobs"
    }


    override fun generateItemStack(id: String): ItemStack? {
        return MythicBukkit.inst().itemManager.getItemStack(id)
    }


}