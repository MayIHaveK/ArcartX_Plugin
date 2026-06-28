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
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** 声音相关语句 */
object EntitySound {

    /**
     * 给实体附近可见玩家播放声音。
     *
     * 参数顺序：
     * 0 - soundPath  声音资源路径
     * 1 - soundCategory  声音分类（如 master）
     * 2 - distOrRoll  距离/衰减
     * 3 - pitch  音调
     * 4 - keepTime  保持时间（毫秒）
     */
    @JvmStatic
    @AriaInvokeHandler(value = "playSound", target = LivingEntity::class)
    fun playNearby(data: InvocationData) {
        if (data.size() != 5) return
        val entity = data.target as LivingEntity

        val soundPath = data[0].stringValue()
        val soundCategory = data[1].stringValue()
        val distOrRoll = data[2].intValue()
        val pitch = data[3].doubleValue()
        val keepTime = data[4].intValue()

        ArcartXEntityManager.getOrCreateEntity(entity)?.playSound(
            soundPath,
            soundCategory,
            distOrRoll,
            pitch,
            keepTime
        )
    }

}
