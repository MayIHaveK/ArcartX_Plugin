/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import org.bukkit.Location
import priv.seventeen.artist.arcartx.core.effect.ArcartXEffectManager.spawnBedrockParticle
import priv.seventeen.artist.arcartx.core.effect.data.EffectPosition
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor

/** 基岩版粒子命令 */
class BedrockParticleCommand : MultiCommandExecutor() {
    override val name: String
        get() = "bedrockParticle"

    @CommandHandler(
        command = "world",
        description = "在玩家所在世界坐标生成基岩粒子",
        permission = "arcartx.command.admin.bedrockparticle",
        args = ["player","id","x", "y","z","yaw","pitch"],
        senderType = SenderType.OP
    )
    fun spawnWorld(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val x = context.getArgAsDouble(2) ?: return
            val y = context.getArgAsDouble(3) ?: return
            val z = context.getArgAsDouble(4) ?: return
            val yaw = context.getArgAsFloat(5) ?: return
            val pitch = context.getArgAsFloat(6) ?: return
            val loc = Location(it.world, x, y, z, yaw, pitch)
            it.world.spawnBedrockParticle(loc,context.getArg(1), EffectPosition.location(loc))
        }
    }


    @CommandHandler(
        command = "player",
        description = "在玩家身上生成粒子",
        permission = "arcartx.command.admin.bedrockparticle",
        args = ["player","id","xOffset", "yOffset","zOffset","yaw","pitch", "isFollowYaw", "isFollowPitch"],
        senderType = SenderType.OP
    )
    fun spawnPlayer(context: CommandContext) {

        context.getArgAsPlayer(0)?.let {
            val xOffset = context.getArgAsDouble(2) ?: return
            val yOffset = context.getArgAsDouble(3) ?: return
            val zOffset = context.getArgAsDouble(4) ?: return
            val pitch = context.getArgAsFloat(6) ?: return
            val yaw = context.getArgAsFloat(5) ?: return
            val isFollowPitch = context.getArgAsBoolean(8) ?: return
            val isFollowYaw = context.getArgAsBoolean(7) ?: return
            it.spawnBedrockParticle(context.getArg(1), EffectPosition.followEntity(it,
                xOffset, yOffset, zOffset,
                pitch, yaw, 0F,
                isFollowPitch, isFollowYaw))

        }
    }


}