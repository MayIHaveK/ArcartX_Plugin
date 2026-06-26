/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.util.PlayerUtils.arcartXHandler

/** 伤害显示命令 */
class DamageDisplayCommand : MultiCommandExecutor() {

    override val name: String
        get() = "damageDisplay"

    @CommandHandler(
        command = "test",
        description = "测试伤害显示",
        permission = "arcartx.command.admin.damagedisplay",
        args = ["configId","x","y","z","damage","?player"],
        senderType = SenderType.PLAYER
    )
    fun test(context: CommandContext) {
        val player = if(context.getArgSize() == 6){
            context.getArgAsPlayer(5)
        } else{
            context.getSender() as Player
        }

        val x = context.getArgAsDouble(1) ?: return
        val y = context.getArgAsDouble(2) ?: return
        val z = context.getArgAsDouble(3) ?: return
        val damage = context.getArgAsDouble(4) ?: return
        player?.arcartXHandler?.addDamageDisplay(
            context.getArg(0),
            x,
            y,
            z,
            damage,
        )
    }

    @CommandHandler(
        command = "testSelf",
        description = "测试伤害显示",
        permission = "arcartx.command.admin.damagedisplay",
        args = ["configId","damage"],
        senderType = SenderType.PLAYER
    )
    fun testSelf(context: CommandContext) {
        val player = context.getSender() as Player

        val damage = context.getArgAsDouble(1) ?: return
        player.arcartXHandler?.addDamageDisplay(
            context.getArg(0),
            player,
            damage,
        )
    }
}