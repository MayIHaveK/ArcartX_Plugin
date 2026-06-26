/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.command.executor

import priv.seventeen.artist.arcartx.commons.command.AXCommandFunction
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType

/** 单命令执行器 */
abstract class SingleCommandExecutor : AXCommandExecutor {

    val function: AXCommandFunction by lazy {
        AXCommandFunction(
            this.javaClass.getMethod("execute", CommandContext::class.java),
            permission,
            only,
            description,
            argsDescription
        )
    }



    abstract override val name: String

    protected abstract val permission: String

    protected abstract val only: SenderType

    abstract override val description: String

    abstract val argsDescription: Array<String>

    abstract fun execute(context: CommandContext)
}
