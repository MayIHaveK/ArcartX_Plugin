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
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager

class BoundingBox(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val width: PlaceholderDouble = mlc.getPlaceholderDouble(arrayOf("w", "width"), 1.0)

    private val height: PlaceholderDouble = mlc.getPlaceholderDouble(arrayOf("h", "height"), 1.0)

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        ArcartXEntityManager.getOrCreateEntity(entity)?.setSize(width.get(skillMetadata), height.get(skillMetadata))
    }
}
