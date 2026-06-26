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
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.util.EntityUtils.doWithSeenBy
import priv.seventeen.artist.arcartx.util.PlayerUtils.arcartXHandler

class BedrockParticle(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val id = mlc.getPlaceholderString(arrayOf("id", "pid"), "")

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        entity.doWithSeenBy() { player ->
            player.arcartXHandler?.spawnBedrockParticle(id.get(skillMetadata), entity.location.x, entity.location.y, entity.location.z, entity.location.yaw, 0F)
        }
    }
}
