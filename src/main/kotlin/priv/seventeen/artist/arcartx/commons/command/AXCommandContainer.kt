/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.command

import org.bukkit.Server
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L

/** 命令注册容器 */
class AXCommandContainer(private val plugin: JavaPlugin) {

    private val fallbackPrefix = plugin.description.name

    fun getFallbackPrefix(): String {
        return fallbackPrefix
    }

    fun register(command: AXCommand) {
        command.setPlugin(this.plugin)
        val serverClass: Class<out Server?> = plugin.server.javaClass

        try {
            val getCommandMap = serverClass.getDeclaredMethod("getCommandMap")
            val commandMap = getCommandMap.invoke(plugin.server) as CommandMap
            commandMap.register(this.fallbackPrefix, command)
        } catch (e: Exception) {
            // 统一走 sendException：保留完整堆栈，便于定位（旧实现只打 e.message 丢堆栈）
            plugin.sendException(L(AXLanguageKey.EXCEPTION_COMMAND_REGISTER), e)
        }
    }
}