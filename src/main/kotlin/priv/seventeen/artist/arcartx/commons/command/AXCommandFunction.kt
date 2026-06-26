/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.command

import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import java.lang.reflect.Method

/** 命令函数元数据 */
class AXCommandFunction {

     var permission = ""
         private set

    var only: SenderType? = null
        private set

     var description = ""
         private set

    var argsDescription = arrayOf<String>()
        private set

    val method: Method


    constructor(method: Method, commandHandler: CommandHandler) {
        this.permission = commandHandler.permission
        this.only = commandHandler.senderType
        this.description = commandHandler.description
        this.argsDescription = commandHandler.args
        this.method = method

        require(!(method.parameterCount != 1 || method.parameterTypes[0] != CommandContext::class.java)) { "代码错误:" + method.name + "参数应该为CommandContext" }
    }

    constructor(
        method: Method,
        permission: String,
        only: SenderType?,
        description: String,
        argsDescription: Array<String>
    ) {
        this.method = method
        this.permission = permission
        this.argsDescription = argsDescription
        this.only = only
        this.description = description

        require(!(method.parameterCount != 1 || method.parameterTypes[0] != CommandContext::class.java)) { "代码错误:" + method.name + "参数应该为CommandContext" }
    }
}