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
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.network.NetworkMessageSender

class CameraShake(holder: CustomMechanic?, mlc: MythicLineConfig) : ITargetedEntitySkill {

    private val duration: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("d", "duration"), 20)
    private val amplitude: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("a", "amplitude"), 14)

    override fun castAtEntity(skillMetadata: SkillMetadata, abstractEntity: AbstractEntity): SkillResult {
        val entity = BukkitAdapter.adapt(abstractEntity)
        if (entity !is Player) {
            return SkillResult.SUCCESS
        }
        NetworkMessageSender.sendShake(entity, duration.get(skillMetadata), amplitude.get(skillMetadata))
        return SkillResult.SUCCESS
    }
}
