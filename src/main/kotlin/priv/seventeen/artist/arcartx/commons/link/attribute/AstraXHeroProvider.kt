/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.attribute

import cn.bukkitmc.hero.AttributeAPI.addSourAttr
import cn.bukkitmc.hero.AttributeAPI.readAttribute
import cn.bukkitmc.hero.AttributeAPI.readAttributes
import cn.bukkitmc.hero.AttributeAPI.takeSourAttr
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

class AstraXHeroProvider : AttributeProvider {

    override fun getIdentifier(): String {
        return "AstraXHero"
    }

    override fun addAttribute(livingEntity: LivingEntity, sourceID: String, list: List<String>) {
        val data = list.readAttributes()
        livingEntity.addSourAttr(sourceID, data)
    }

    override fun addAttribute(livingEntity: LivingEntity, sourceID: String, itemStack: ItemStack) {
        val data = itemStack.readAttribute()
        livingEntity.addSourAttr(sourceID, data)
    }

    override fun removeAttribute(livingEntity: LivingEntity, sourceID: String) {
        livingEntity.takeSourAttr(sourceID)
    }
}