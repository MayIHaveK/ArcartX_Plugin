/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs.mechanic

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler.ensureMainThread
import priv.seventeen.artist.blink.bukkitPlugin

/**
 * 实体机制基类：统一「adapt 目标实体 → 切主线程执行 → 返回 SUCCESS」的模板。
 */
abstract class AbstractEntityMechanic(
    @Suppress("UNUSED_PARAMETER") holder: CustomMechanic?
) : ITargetedEntitySkill {

    final override fun castAtEntity(skillMetadata: SkillMetadata, abstractEntity: AbstractEntity): SkillResult {
        val entity = BukkitAdapter.adapt(abstractEntity)
        bukkitPlugin.ensureMainThread { run(entity, skillMetadata) }
        return SkillResult.SUCCESS
    }

    protected abstract fun run(entity: Entity, skillMetadata: SkillMetadata)
}
