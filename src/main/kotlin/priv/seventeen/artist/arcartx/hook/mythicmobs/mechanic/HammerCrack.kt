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
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import priv.seventeen.artist.arcartx.util.EntityUtils.doWithSeenBy
import priv.seventeen.artist.arcartx.util.PlayerUtils.arcartXHandler

class HammerCrack(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val radius: PlaceholderFloat = mlc.getPlaceholderFloat(arrayOf("radius","r"), 3.0f)
    private val depth: PlaceholderFloat = mlc.getPlaceholderFloat(arrayOf("depth","d"), 0.3f)
    private val `in`: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("in","i"), 10)
    private val keep: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("keep","k"), 40)
    private val out: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("out","o"), 20)
    private val mode: PlaceholderInt = mlc.getPlaceholderInteger(arrayOf("mode","m"), 0)

    // hammerEffect{radius=3, depth=0.3, in=10, keep=40, out=20, mode=0}
    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        val location = entity.location
        entity.doWithSeenBy({
            it.arcartXHandler?.spawnHammerCrackEffect(
                location.x.toInt(), location.y.toInt(), location.z.toInt(),
                radius.get(skillMetadata), depth.get(skillMetadata),
                `in`.get(skillMetadata), keep.get(skillMetadata), out.get(skillMetadata), mode.get(skillMetadata)
            )
        })
    }
}
