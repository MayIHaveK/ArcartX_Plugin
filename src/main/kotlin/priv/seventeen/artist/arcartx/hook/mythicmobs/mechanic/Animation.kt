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

class Animation(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val animation: PlaceholderString = mlc.getPlaceholderString(arrayOf("n", "name"), "idle")

    private val transitionTime: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("t", "transitionTime"), 5)

    private val speed: PlaceholderDouble = mlc.getPlaceholderDouble(arrayOf("s", "speed"), 1.0)

    private val time: PlaceholderString = mlc.getPlaceholderString(arrayOf("k", "time"), "-1")

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        ArcartXEntityManager.getOrCreateEntity(entity)?.playAnimation(
            animation.get(skillMetadata),
            speed.get(skillMetadata),
            transitionTime.get(skillMetadata),
            time.get(skillMetadata).toLongOrNull() ?: -1L
        )
    }
}
