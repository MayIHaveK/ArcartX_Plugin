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
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.network.NetworkMessageSender

/** 界面管理命令 */
class ScreenCommand : MultiCommandExecutor() {

    override val name: String
        get() = "screen"

    @CommandHandler(
        command = "open",
        description = "打开UI",
        permission = "arcartx.command.admin.screen",
        args = ["id","?player"],
        senderType = SenderType.OP
    )
    fun open(context: CommandContext) {
        if(context.getArgSize() == 2){
            context.getArgAsPlayer(1)?.let {
                NetworkMessageSender.sendOpenUI(it, context.getArg(0))
                return
            }
        }
        if(context.getSender() is Player){
            val player = context.getSender() as Player
            NetworkMessageSender.sendOpenUI(player, context.getArg(0))
        }

    }

    @CommandHandler(
        command = "close",
        description = "关闭UI",
        permission = "arcartx.command.admin.screen",
        args = ["id","?player"],
        senderType = SenderType.OP
    )
    fun close(context: CommandContext) {
        if(context.getArgSize() == 2){
            context.getArgAsPlayer(1)?.let {
                NetworkMessageSender.sendCloseUI( it, context.getArg(0))
                return
            }
        }
        if(context.getSender() is Player){
            val player = context.getSender() as Player
            NetworkMessageSender.sendCloseUI( player, context.getArg(0))
        }
    }

    @CommandHandler(
        command = "slotDebug",
        description = "开关调试槽位",
        permission = "arcartx.command.admin.screen",
        args = ["true/false"],
        senderType = SenderType.PLAYER
    )
    fun slotDebug(context: CommandContext) {
        val player = context.getSender() as Player
        val debug = context.getArgAsBoolean(0) ?: return
        NetworkMessageSender.sendSlotDebug(player, debug)
        context.sendMessage(L(AXLanguageKey.SLOT_DEBUG_MODE, debug.toString()))
    }








}