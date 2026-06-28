/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs.mechanic

import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedLocationSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.core.effect.ArcartXEffectManager.spawnBedrockParticle
import priv.seventeen.artist.arcartx.core.effect.data.EffectPosition
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler.ensureMainThread
import priv.seventeen.artist.arcartx.util.EntityUtils.doWithSeenBy
import priv.seventeen.artist.arcartx.util.PlayerUtils.arcartXHandler
import priv.seventeen.artist.blink.bukkitPlugin

class BedrockParticle(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder),
    ITargetedLocationSkill {

    private val id = mlc.getPlaceholderString(arrayOf("id", "pid"), "")

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        entity.doWithSeenBy() { player ->
            player.arcartXHandler?.spawnBedrockParticle(id.get(skillMetadata), entity.location.x, entity.location.y, entity.location.z, entity.location.yaw, 0F)
        }
    }

    override fun castAtLocation(skillMetadata: SkillMetadata, abstractLocation: AbstractLocation): SkillResult {
        val location = BukkitAdapter.adapt(abstractLocation)
        bukkitPlugin.ensureMainThread {
            location.world?.spawnBedrockParticle(location, id.get(skillMetadata), EffectPosition.location(location))
        }
        return SkillResult.SUCCESS
    }
}
