/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.aria.target

import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** 战斗相关语句 */
object EntityCombat {

    @JvmStatic
    @AriaInvokeHandler(value = "attack", target = LivingEntity::class)
    fun damageNearby(data: InvocationData) {
        if (data.size() != 1) return
        val entity = data[0].jvmValue() as? LivingEntity ?: return
        val player = data.target as LivingEntity
        // 守卫：跨世界或目标已死亡时 Bukkit attack 行为未定义，直接跳过
        if (entity.isDead || entity.world != player.world) return
        player.attack(entity)
    }

    @JvmStatic
    @AriaInvokeHandler(value = "setHealth", target = LivingEntity::class)
    fun health(data: InvocationData) {
        if (data.size() < 1) return
        val amount = data[0].doubleValue()
        val player = data.target as LivingEntity
        // 钳制到 [0, 最大血量]，否则脚本传越界值会让 Bukkit 抛 IllegalArgumentException 中断脚本
        val max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        player.health = amount.coerceIn(0.0, max)
    }

    @JvmStatic
    @AriaInvokeHandler(value = "setFire", target = LivingEntity::class)
    fun setFireNearby(data: InvocationData) {
        if (data.size() < 1) return
        val ticks = data[0].intValue()
        val player = data.target as LivingEntity
        player.fireTicks = ticks
    }
}
