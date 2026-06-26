/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import ink.ptms.adyeshach.module.command.Command
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor

/** Adyeshach 集成命令 */
class AdyeshachCommand : MultiCommandExecutor() {

    override val name: String
        get() = "adyeshach"

    @CommandHandler(
        command = "setModel",
        description = "设置就近单位模型",
        permission = "arcartx.command.admin.adyeshach",
        args = ["modelID","scale"],
        senderType = SenderType.PLAYER
    )
    fun setModel(context: CommandContext) {
        (context.getSender() as Player).let {
            val scale = context.getArgAsDouble(1) ?: return
            val nearestEntity = Command.finder.getNearestEntity(it) { entity -> !entity.isDerived() }
            nearestEntity?.setPersistentTag("ArcartX_Model",context.getArg(0))
            nearestEntity?.setPersistentTag("ArcartX_Scale",scale.toString())
        }
    }

    @CommandHandler(
        command = "setBox",
        description = "设置就近单位碰撞体积",
        permission = "arcartx.command.admin.adyeshach",
        args = ["width","height"],
        senderType = SenderType.PLAYER
    )
    fun setBox(context: CommandContext) {
        (context.getSender() as Player).let {
            val width = context.getArgAsDouble(0) ?: return
            val height = context.getArgAsDouble(1) ?: return
            val nearestEntity = Command.finder.getNearestEntity(it) { entity -> !entity.isDerived() }
            nearestEntity?.setPersistentTag("ArcartX_Width",width.toString())
            nearestEntity?.setPersistentTag("ArcartX_Height",height.toString())
        }
    }

}