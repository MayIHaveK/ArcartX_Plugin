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
import io.lumine.mythic.api.skills.placeholders.PlaceholderFloat
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox.InteractionProxyManager

class ProxyMountSpeed(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val moveSpeed: PlaceholderFloat = mlc.getPlaceholderFloat(arrayOf("moveSpeed", "s"), 0.3F)
    private val flyUpSpeed: PlaceholderFloat = mlc.getPlaceholderFloat(arrayOf("flyUpSpeed","fu"), 0.08F)
    private val flyDownSpeed: PlaceholderFloat = mlc.getPlaceholderFloat(arrayOf("flyDownSpeed","fd"), 0.06F)
    private val boatTurnSpeed: PlaceholderFloat = mlc.getPlaceholderFloat(arrayOf("boatTurnSpeed","bt"), 2.0F)

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        InteractionProxyManager.setMountSpeed(entity, moveSpeed.get(skillMetadata), flyUpSpeed.get(skillMetadata), flyDownSpeed.get(skillMetadata), boatTurnSpeed.get(skillMetadata))
    }
}
