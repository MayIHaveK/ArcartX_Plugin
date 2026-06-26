/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.aria.target

import cn.bukkitmc.hero.astrax.compat.placeholder.XPlaceholderHolder.placeholder
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.util.PlayerUtils.arcartXHandler
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** 玩家信息 */
object PlayerInfo {

    @JvmStatic
    @AriaInvokeHandler(value = "isAfterJump", target = Player::class)
    fun isAfterJump(data: InvocationData): Boolean {
        val arcartXPlayer = (data.target as Player).arcartXHandler ?: return false
        return arcartXPlayer.jumpTime > System.currentTimeMillis()
    }

    @JvmStatic
    @AriaInvokeHandler(value = "isSprinting", target = Player::class)
    fun isRun(data: InvocationData): Boolean {
        val player = data.target as Player
        return player.isSprinting
    }

    @JvmStatic
    @AriaInvokeHandler(value = "isSneaking", target = Player::class)
    fun isSneak(data: InvocationData): Boolean {
        val player = data.target as Player
        return player.isSneaking
    }

    @JvmStatic
    @AriaInvokeHandler(value = "isFlying", target = Player::class)
    fun isFlying(data: InvocationData): Boolean {
        val player = data.target as Player
        return player.isFlying
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getFood", target = Player::class)
    fun getFood(data: InvocationData): Int {
        val player = data.target as Player
        return player.foodLevel
    }

    @JvmStatic
    @AriaInvokeHandler(value = "getPlaceholder", target = Player::class)
    fun getPlaceholder(data: InvocationData): String {
        if (data.size() != 1) return ""
        val player = data.target as Player
        return data.get(0).stringValue().placeholder(player)
    }


}
