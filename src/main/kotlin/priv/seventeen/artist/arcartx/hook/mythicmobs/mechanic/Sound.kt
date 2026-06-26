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
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager

class Sound(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val soundPath: PlaceholderString = mlc.getPlaceholderString(arrayOf("s", "sound"), "null")
    private val soundCategory: PlaceholderString = mlc.getPlaceholderString(arrayOf("c", "category"), "master")

    private val distOrRoll: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("d", "distOrRoll"), 16)
    private val pitch: PlaceholderDouble = mlc.getPlaceholderDouble(arrayOf("p", "pitch"), 1.0)
    private val keepTime: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("k", "keepTime"), 2000)

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        ArcartXEntityManager.getOrCreateEntity(entity)?.playSound(
            soundPath.get(skillMetadata),
            soundCategory.get(skillMetadata),
            distOrRoll.get(skillMetadata),
            pitch.get(skillMetadata),
            keepTime.get(skillMetadata)
        )
    }
}
