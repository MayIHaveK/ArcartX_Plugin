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
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.SingleCommandExecutor
import priv.seventeen.artist.arcartx.network.NetworkMessageSender

/** 解析器命令 */
class ParserCommand(
    override val name: String = "parser",
    override val permission: String = "arcartx.command.admin.parser",
    override val only: SenderType = SenderType.OP,
    override val description: String = "运行脚本",
    override val argsDescription: Array<String> = arrayOf("player", "code", "async"),
) : SingleCommandExecutor() {

    override fun execute(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val code: String = context.getArg(1)
            val async: Boolean = context.getArgAsBoolean(2) ?: return
            NetworkMessageSender.sendExecuteScript(it, code, async)
        }
    }
}