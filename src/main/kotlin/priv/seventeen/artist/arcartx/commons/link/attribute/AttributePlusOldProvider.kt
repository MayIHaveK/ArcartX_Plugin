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
import org.serverct.ersha.api.AttributeAPI


class AttributePlusOldProvider : AttributeProvider {

    override fun getIdentifier(): String {
        return "AttributePlus"
    }

    override fun addAttribute(livingEntity: LivingEntity, sourceID: String, list: List<String>) {
        val data: org.serverct.ersha.attribute.data.AttributeData = AttributeAPI.getAttrData(livingEntity)
        AttributeAPI.addSourceAttribute(data, sourceID,list)
    }

    override fun addAttribute(livingEntity: LivingEntity, sourceID: String, itemStack: ItemStack) {
        val data: org.serverct.ersha.attribute.data.AttributeData = AttributeAPI.getAttrData(livingEntity)
        if(!itemStack.type.isAir){
            AttributeAPI.addSourceAttribute(data, sourceID,itemStack)
        }
    }

    override fun removeAttribute(livingEntity: LivingEntity, sourceID: String) {
        val data: org.serverct.ersha.attribute.data.AttributeData = AttributeAPI.getAttrData(livingEntity)
        AttributeAPI.takeSourceAttribute(data, sourceID)
    }
}