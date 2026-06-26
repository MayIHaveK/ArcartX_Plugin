/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.SingleCommandExecutor
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L

/** 重载命令 */
class ReloadCommand(
    override val name: String = "reload",
    override val permission: String = "arcartx.command.reload",
    override val only: SenderType = SenderType.OP,
    override val description: String = "重载插件",
    override val argsDescription: Array<String> = arrayOf("?withResource")
) : SingleCommandExecutor() {
    override fun execute(context: CommandContext) {
        context.sendMessage(L(AXLanguageKey.RELOADING))
        ArcartX.configs.reload(context.getArgSize() > 0 && context.getArg(0).toBoolean())
        context.sendMessage(L(AXLanguageKey.RELOAD_COMPLETE))
    }
}