/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import java.util.*

/** 音效播放命令 */
class SoundCommand : MultiCommandExecutor() {

    override val name: String
        get() = "sound"

    @CommandHandler(
        command = "location",
        description = "定点播放音效",
        permission = "arcartx.command.admin.sound",
        args = ["player", "resourcePath", "x", "y", "z", "soundCategory", "distOrRoll", "pitch", "keepTime"],
        senderType = SenderType.OP
    )
    fun location(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val x = context.getArgAsInt(2) ?: return
            val y = context.getArgAsInt(3) ?: return
            val z = context.getArgAsInt(4) ?: return
            val distOrRoll = context.getArgAsInt(6) ?: return
            val pitch = context.getArgAsDouble(7) ?: return
            val keepTime = context.getArgAsInt(8) ?: return
            NetworkMessageSender.sendPlaySound(
                it, context.getArg(1),
                x, y, z,
                context.getArg(5), distOrRoll, pitch, keepTime
            )
        }
    }

    @CommandHandler(
        command = "entity",
        description = "绑定实体播放音效",
        permission = "arcartx.command.admin.sound",
        args = ["player", "resourcePath", "soundCategory", "player", "distOrRoll", "pitch", "keepTime"],
        senderType = SenderType.OP
    )
    fun entity(context: CommandContext) {
        val player: Player? = context.getArgAsPlayer(0)
        if (player != null) {
            val name: String = context.getArg(3)
            var target: UUID? = null
            try {
                target = UUID.fromString(name)
            } catch (e: Exception) {
                val p = Bukkit.getPlayerExact(name)
                if (p != null) {
                    target = p.uniqueId
                }
            }
            if (target != null) {
                val distOrRoll = context.getArgAsInt(4) ?: return
                val pitch = context.getArgAsDouble(5) ?: return
                val keepTime = context.getArgAsInt(6) ?: return
                NetworkMessageSender.sendPlayEntitySound(
                    player, context.getArg(1), target.toString(),
                    context.getArg(2), distOrRoll, pitch, keepTime
                )
            }
        }
    }
}