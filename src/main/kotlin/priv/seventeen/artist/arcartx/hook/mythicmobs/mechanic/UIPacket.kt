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
import io.lumine.mythic.api.skills.placeholders.PlaceholderString
import io.lumine.mythic.core.skills.mechanics.CustomMechanic
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.api.ArcartXAPI

class UIPacket(holder: CustomMechanic?, mlc: MythicLineConfig) : AbstractEntityMechanic(holder) {

    private val ui: PlaceholderString = mlc.getPlaceholderString(arrayOf("u", "ui"), "")
    private val packetHandler: PlaceholderString = mlc.getPlaceholderString(arrayOf("ph", "packetHandler"), "")
    private val pack: PlaceholderString = mlc.getPlaceholderString(arrayOf("pack", "p"), "")

    override fun run(entity: Entity, skillMetadata: SkillMetadata) {
        val uiVal = ui.get(skillMetadata)
        val phVal = packetHandler.get(skillMetadata)
        if (!(uiVal.isEmpty() || phVal.isEmpty() || entity !is Player)) {
            ArcartXAPI.getUIRegistry().sendPacket(entity, uiVal, phVal, pack.get(skillMetadata))
        }
    }
}
