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
import org.bukkit.entity.Player
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** 实体信息 */
object EntityInfo {


    @JvmStatic
    @AriaInvokeHandler(value = "isInWater", target = LivingEntity::class)
    fun isInWater(data: InvocationData): Boolean {
        val player = data.target as LivingEntity
        return player.isInWater
    }

    @JvmStatic
    @AriaInvokeHandler(value = "isFallFlying", target = LivingEntity::class)
    fun isFallFlying(data: InvocationData): Boolean {
        val player = data.target as LivingEntity
        return player.isGliding
    }

    @JvmStatic
    @AriaInvokeHandler(value = "isSwimming", target = LivingEntity::class)
    fun isSwimming(data: InvocationData): Boolean {
        val player = data.target as LivingEntity
        return player.isSwimming
    }


    @JvmStatic
    @AriaInvokeHandler(value = "isSleeping", target = LivingEntity::class)
    fun isSleeping(data: InvocationData): Boolean {
        val player = data.target as LivingEntity
        return player.isSleeping
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getPosX", target = LivingEntity::class)
    fun getPosX(data: InvocationData): Double {
        val player = data.target as LivingEntity
        return player.location.x
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getPosY", target = LivingEntity::class)
    fun getPosY(data: InvocationData): Double {
        val player = data.target as LivingEntity
        return player.location.y
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getPosZ", target = LivingEntity::class)
    fun getPosZ(data: InvocationData): Double {
        val player = data.target as LivingEntity
        return player.location.z
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getYaw", target = LivingEntity::class)
    fun getYaw(data: InvocationData): Float {
        val player = data.target as LivingEntity
        return player.location.yaw
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getPitch", target = LivingEntity::class)
    fun getPitch(data: InvocationData): Float {
        val player = data.target as LivingEntity
        return player.location.pitch
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getAir", target = LivingEntity::class)
    fun getAir(data: InvocationData): Int {
        val player = data.target as LivingEntity
        return player.remainingAir
    }

    @JvmStatic
    @AriaInvokeHandler(value = "isOnGround", target = LivingEntity::class)
    fun isOnGround(data: InvocationData): Boolean {
        val player = data.target as LivingEntity
        return player.isOnGround
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getHealth", target = LivingEntity::class)
    fun getSelfHealth(data: InvocationData): Double {
        val player = data.target as LivingEntity
        return player.health
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getMaxHealth", target = LivingEntity::class)
    fun getSelfMaxHealth(data: InvocationData): Double {
        val player = data.target as LivingEntity
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
    }

    @JvmStatic
    @AriaInvokeHandler(value = "isPlayer", target = LivingEntity::class)
    fun isPlayer(data: InvocationData): Boolean {
        val player = data.target as LivingEntity
        return player is Player
    }
}
