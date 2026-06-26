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

/** 路标管理命令 */
class WaypointCommand : MultiCommandExecutor() {

    override val name: String
        get() = "waypoint"

    @CommandHandler(
        command = "add",
        description = "添加路标",
        permission = "arcartx.command.admin.waypoint",
        args = ["id","title","configId","x","y","z","?player"],
        senderType = SenderType.OP
    )
    fun add(context: CommandContext) {
        val player = if(context.getArgSize() == 7){
            context.getArgAsPlayer(6)
        } else{
            context.getSender() as Player
        }

        val x = context.getArgAsDouble(3) ?: return
        val y = context.getArgAsDouble(4) ?: return
        val z = context.getArgAsDouble(5) ?: return
        player?.arcartXHandler?.addWayPoint(
            context.getArg(0),
            context.getArg(1),
            context.getArg(2),
            x,
            y,
            z
        )
    }

    @CommandHandler(
        command = "delete",
        description = "删除路标",
        permission = "arcartx.command.admin.waypoint",
        args = ["id","?player"],
        senderType = SenderType.OP
    )
    fun delete(context: CommandContext) {
        if(context.getArgSize() == 2){
            context.getArgAsPlayer(1)?.let {
                it.arcartXHandler?.deleteWayPoint(context.getArg(0))
                return
            }
        }
        val player = context.getSender() as Player
        player.arcartXHandler?.deleteWayPoint(context.getArg(0))
    }

    // clear
    @CommandHandler(
        command = "clear",
        description = "清除路标",
        permission = "arcartx.command.admin.waypoint",
        args = ["?player"],
        senderType = SenderType.OP
    )
    fun clear(context: CommandContext) {
        if(context.getArgSize() == 1){
            context.getArgAsPlayer(0)?.let {
                it.arcartXHandler?.clearWayPoint()
                return
            }
        }
        val player = context.getSender() as Player
        player.arcartXHandler?.clearWayPoint()
    }


}