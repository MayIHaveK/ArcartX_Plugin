/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.aria.target

import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.entity.LivingEntity
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** MythicMobs 技能施放的 Aria 调用封装 */
object MythicMob {

    @JvmStatic
    @AriaInvokeHandler(value = "castMythicMobSkill", target = LivingEntity::class)
    fun castSkill(data: InvocationData) {
        if (data.size() != 2) return
        // 参数: 技能名称, 强度
        val name = data[0].stringValue()
        val power = data[1].floatValue()
        MythicBukkit.inst().apiHelper.castSkill(data.target as LivingEntity, name, power)
    }

}