/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.attribute

import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

class CraneAttributeProvider : AttributeProvider {
    private val accessor = `CraneAttribute$Accessor`()

    override fun getIdentifier(): String {
        return "CraneAttribute"
    }

    override fun addAttribute(livingEntity: LivingEntity, sourceID: String, list: List<String>) {
        accessor.addAttribute(livingEntity, sourceID, list)
    }

    override fun addAttribute(livingEntity: LivingEntity, sourceID: String, itemStack: ItemStack) {
        accessor.addAttribute(livingEntity, sourceID, itemStack)
    }

    override fun removeAttribute(livingEntity: LivingEntity, sourceID: String) {
        accessor.removeAttribute(livingEntity, sourceID)
    }
}