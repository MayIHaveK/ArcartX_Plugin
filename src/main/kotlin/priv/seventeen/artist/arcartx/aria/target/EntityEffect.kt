/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.aria.target

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** 药水效果和状态相关语句 */
object EntityEffect {

    @JvmStatic
    @AriaInvokeHandler(value = "addPotionEffect", target = LivingEntity::class)
    fun addPotionEffect(data: InvocationData) {
        if (data.size() != 3) return
        val id = data[0].stringValue()
        val duration = data[1].intValue()
        val amplifier = data[2].intValue()

        val player = data.target as LivingEntity
        val effectType = PotionEffectType.getByName(id) ?: return
        player.addPotionEffect(PotionEffect(effectType, duration, amplifier))
    }

    @JvmStatic
    @AriaInvokeHandler(value = "removePotionEffect", target = LivingEntity::class)
    fun removeEffect(data: InvocationData) {
        if (data.size() != 1) return
        val effectName = data[0].stringValue()
        val player = data.target as LivingEntity
        val type = PotionEffectType.getByName(effectName) ?: return
        player.removePotionEffect(type)
    }

    @JvmStatic
    @AriaInvokeHandler(value = "clearPotionEffect", target = LivingEntity::class)
    fun clearEffects(data: InvocationData) {
        val player = data.target as LivingEntity
        player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
    }

    @JvmStatic
    @AriaInvokeHandler(value = "hasPotionEffect", target = LivingEntity::class)
    fun hasEffect(data: InvocationData): Boolean {
        if (data.size() != 1) return false
        val effectName = data[0].stringValue()
        val player = data.target as LivingEntity
        val type = PotionEffectType.getByName(effectName) ?: return false
        return player.hasPotionEffect(type)
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getPotionEffectDuration", target = LivingEntity::class)
    fun getEffectDuration(data: InvocationData): Int {
        if (data.size() != 1) return 0
        val effectName = data[0].stringValue()
        val player = data.target as LivingEntity
        val type = PotionEffectType.getByName(effectName) ?: return 0
        return player.getPotionEffect(type)?.duration ?: 0
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getPotionEffectAmplifier", target = LivingEntity::class)
    fun getEffectAmplifier(data: InvocationData): Int {
        if (data.size() != 1) return 0
        val effectName = data[0].stringValue()
        val player = data.target as LivingEntity
        val type = PotionEffectType.getByName(effectName) ?: return -1
        return player.getPotionEffect(type)?.amplifier ?: -1
    }
}
