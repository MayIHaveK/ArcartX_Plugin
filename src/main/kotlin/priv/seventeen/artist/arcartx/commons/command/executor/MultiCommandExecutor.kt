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
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler

/** 多子命令执行器 */
abstract class MultiCommandExecutor : AXCommandExecutor {

    val functionMap = HashMap<String, AXCommandFunction>()

    private val names: MutableMap<String, String> = HashMap()


    init {
        this.registerCommands()
    }

    abstract override val name: String

    override val description: String
        get() = "查看该分类下命令详细内容"

    fun getFunction(subCommand: String): AXCommandFunction? {
        return functionMap[names[subCommand.lowercase()]]
    }

    private fun registerCommands() {
        for (method in javaClass.declaredMethods) {
            method.isAccessible = true
            if (method.isAnnotationPresent(CommandHandler::class.java)) {
                val commandHandler: CommandHandler = method.getAnnotation(CommandHandler::class.java)
                val subName: String = commandHandler.command
                functionMap[subName] = AXCommandFunction(method, commandHandler)
                names[subName.lowercase()] = subName
            }
        }
    }
}