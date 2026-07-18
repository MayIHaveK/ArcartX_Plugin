/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs.mechanic

import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox.InteractionProxyManager

class HitboxProxy(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val width: PlaceholderDouble = mlc.getPlaceholderDouble(arrayOf("w", "width"), 1.0)
    private val height: PlaceholderDouble = mlc.getPlaceholderDouble(arrayOf("h", "height"), 1.0)
    private val remove: PlaceholderString = mlc.getPlaceholderString(arrayOf("r", "remove"), "false")
    private val update: PlaceholderString = mlc.getPlaceholderString(arrayOf("u", "update"), "false")

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        val removeVal = remove.get(skillMetadata).toBoolean()
        val updateVal = update.get(skillMetadata).toBoolean()
        val widthVal = width.get(skillMetadata)
        val heightVal = height.get(skillMetadata)

        when {
            removeVal -> {
                // 移除代理碰撞箱
                InteractionProxyManager.removeProxy(entity)
            }
            updateVal -> {
                // 更新现有代理的大小
                if (InteractionProxyManager.hasProxy(entity)) {
                    InteractionProxyManager.updateProxySize(entity, widthVal, heightVal)
                } else {
                    // 如果没有代理，则创建一个
                    InteractionProxyManager.createProxy(entity, widthVal, heightVal)
                }
            }
            else -> {
                // 创建新的代理碰撞箱
                InteractionProxyManager.createProxy(entity, widthVal, heightVal)
            }
        }
        ArcartXEntityManager.getOrCreateEntity(entity)?.setSize(entity.width, heightVal)
    }
}
