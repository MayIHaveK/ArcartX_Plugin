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

/** 天空盒命令 */
class SkyBoxCommand : MultiCommandExecutor() {

    override val name: String
        get() = "skybox"

    @CommandHandler(
        command = "setup",
        description = "为一个玩家设置天空盒贴图",
        permission = "arcartx.command.admin.skybox",
        args = ["player", "path", "force-no-cloud"],
        senderType = SenderType.OP
    )
    fun setup(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val forceNoCloud = context.getArgAsBoolean(2) ?: return
            NetworkMessageSender.sendSetSkyBoxTexture(it, context.getArg(1), forceNoCloud)
        }
    }

    @CommandHandler(
        command = "clear",
        description = "为一个玩家清除天空盒贴图",
        permission = "arcartx.command.admin.skybox",
        args = ["player"],
        senderType = SenderType.OP
    )
    fun clear(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            NetworkMessageSender.sendClearSkyBoxTexture(it)
        }
    }
}