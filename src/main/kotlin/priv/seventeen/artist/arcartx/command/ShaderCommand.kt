/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.network.NetworkMessageSender

/** 着色器命令 */
class ShaderCommand : MultiCommandExecutor() {

    override val name: String
        get() = "shader"

    @CommandHandler(
        command = "start",
        description = "为一个玩家开启一个着色器",
        permission = "arcartx.command.admin.shader",
        args = ["player", "shaderName"],
        senderType = SenderType.OP
    )
    fun start(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            NetworkMessageSender.sendStartShader(it, context.getArg(1))
        }
    }

    @CommandHandler(
        command = "stop",
        description = "为一个玩家关闭着色器",
        permission = "arcartx.command.admin.shader",
        args = ["player"],
        senderType = SenderType.OP
    )
    fun stop(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            NetworkMessageSender.sendCloseShader(it)
        }
    }
}