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

class Model(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {
    private val modelPath: PlaceholderString = mlc.getPlaceholderString(arrayOf("m", "model"), "")

    private val scale: PlaceholderDouble = mlc.getPlaceholderDouble(arrayOf("s", "scale"), 1.0)

    private val reset: PlaceholderString = mlc.getPlaceholderString(arrayOf("r", "reset"), "true")

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        ArcartXEntityManager.getOrCreateEntity(entity)?.setModel(modelPath.get(skillMetadata), scale.get(skillMetadata), reset.get(skillMetadata).toBoolean())
    }
}
