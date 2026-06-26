/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.command.exception.ArgumentIndexException
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import java.util.*

/** 命令执行上下文 */
class CommandContext(
    private val plugin: JavaPlugin,
    private val sender: CommandSender,
    private val function: AXCommandFunction,
    private val args: Array<String>
) {
    fun getArg(index: Int): String {
        return try {
            getArgAsString(index)
        } catch (e: ArgumentIndexException) {
            ""
        }
    }

    fun getArgSize(): Int {
        return args.size
    }

    fun getSender(): CommandSender {
        return sender
    }

    @Throws(ArgumentIndexException::class)
    private fun getArgAsString(index: Int): String {
        try {
            return args[index]
        } catch (exception: Exception) {
            throw ArgumentIndexException("代码错误，下标越界", index)
        }
    }

    fun getArgAsInt(index: Int): Int? {
        try {
            val `val` = getArgAsString(index)
            return if (`val`.contains(".")) {
                `val`.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
            } else {
                `val`.toInt()
            }
        } catch (exception: ArgumentIndexException) {
            return null
        } catch (exception: NumberFormatException) {
            plugin.sendMessage(
                sender,
                L(AXLanguageKey.ARG_NOT_INT, argName(index))
            )
            return null
        }
    }

    fun getArgAsDouble(index: Int): Double? {
        try {
            val `val` = getArgAsString(index)
            return `val`.toDouble()
        } catch (exception: ArgumentIndexException) {
            return null
        } catch (exception: NumberFormatException) {
            plugin.sendMessage(
                sender,
                L(AXLanguageKey.ARG_NOT_DOUBLE, argName(index))
            )
            return null
        }
    }

    fun getArgAsBoolean(index: Int): Boolean? {
        val `val` = try {
            getArgAsString(index)
        } catch (exception: ArgumentIndexException) {
            return null
        }
        return when (`val`.lowercase()) {
            "true" -> true
            "false" -> false
            else -> {
                plugin.sendMessage(sender, L(AXLanguageKey.ARG_NOT_BOOLEAN, argName(index)))
                null
            }
        }
    }

    fun getArgAsLong(index: Int): Long? {
        try {
            val `val` = getArgAsString(index)
            return `val`.toLong()
        } catch (exception: ArgumentIndexException) {
            return null
        } catch (exception: NumberFormatException) {
            plugin.sendMessage(
                sender,
                L(AXLanguageKey.ARG_NOT_LONG, argName(index))
            )
            return null
        }
    }

    fun getArgAsFloat(index: Int): Float? {
        try {
            val `val` = getArgAsString(index)
            return `val`.toFloat()
        } catch (exception: ArgumentIndexException) {
            return null
        } catch (exception: NumberFormatException) {
            plugin.sendMessage(
                sender,
                L(AXLanguageKey.ARG_NOT_FLOAT, argName(index))
            )
            return null
        }
    }

    /** 取参数名用于错误提示，越界时回退到下标，避免在错误路径上再次越界 */
    private fun argName(index: Int): String =
        function.argsDescription.getOrNull(index) ?: index.toString()

    fun getArgAsPlayer(index: Int): Player? {
        try {
            val `val` = getArgAsString(index)
            val player = Bukkit.getPlayerExact(`val`)
            if (player == null) {
                plugin.sendMessage(sender, L(AXLanguageKey.PLAYER_NOT_ONLINE, `val`))
            }
            return player
        } catch (exception: ArgumentIndexException) {
            return null
        }
    }

    fun getArgAsUUID(index: Int): UUID? {
        try {
            val `val` = getArgAsString(index)
            return UUID.fromString(`val`)
        } catch (exception: ArgumentIndexException) {
            return null
        } catch (exception: Exception) {
            plugin.sendMessage(
                sender,
                L(AXLanguageKey.ARG_NOT_UUID, function.argsDescription[index])
            )
            return null
        }
    }

    fun sendMessage(msg: String) {
        plugin.sendMessage(sender, msg)
    }
}