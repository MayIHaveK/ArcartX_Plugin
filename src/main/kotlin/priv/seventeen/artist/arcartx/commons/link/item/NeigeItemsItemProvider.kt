/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.item

import org.bukkit.inventory.ItemStack

/** NeigeItems 物品提供者 */
class NeigeItemsItemProvider: ItemProvider {


    private val accessor = `NeigeItemsItem$Accessor`()

    override fun getIdentifier(): String {
        return "NeigeItems"
    }

    override fun generateItemStack(id: String): ItemStack? {
        return accessor.generateItemStack(id)
    }


}