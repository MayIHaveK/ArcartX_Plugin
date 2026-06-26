/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.papi

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.commons.command.exception.ArgumentIndexException
import java.util.*

/** PAPI 变量解析上下文 */
class PlaceholderContext(val offlinePlayer: OfflinePlayer?, val args: Array<String?>) {


    fun getPlayer(): Player? {
        return offlinePlayer?.let { Bukkit.getPlayer(it.uniqueId) }
    }

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


    @Throws(ArgumentIndexException::class)
    private fun getArgAsString(index: Int): String {
        try {
            return args[index].orEmpty()
        } catch (exception: Exception) {
            throw ArgumentIndexException("代码错误，下标越界", index)
        }
    }

    fun getArgAsInt(index: Int): Int {
        try {
            val `val` = getArgAsString(index)
            return if (`val`.contains(".")) {
                `val`.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
            } else {
                `val`.toInt()
            }
        } catch (exception: ArgumentIndexException) {
            return 0
        } catch (exception: NumberFormatException) {
            return 0
        }
    }

    fun getArgAsDouble(index: Int): Double {
        try {
            val `val` = getArgAsString(index)
            return `val`.toDouble()
        } catch (exception: ArgumentIndexException) {
            return 0.0
        } catch (exception: NumberFormatException) {
            return 0.0
        }
    }

    fun getArgAsBoolean(index: Int): Boolean {
        try {
            val `val` = getArgAsString(index)
            return `val`.toBoolean()
        } catch (exception: ArgumentIndexException) {
            return false
        }
    }

    fun getArgAsLong(index: Int): Long {
        try {
            val `val` = getArgAsString(index)
            return `val`.toLong()
        } catch (exception: ArgumentIndexException) {
            return 0
        } catch (exception: NumberFormatException) {
            return 0
        }
    }

    fun getArgAsFloat(index: Int): Float {
        try {
            val `val` = getArgAsString(index)
            return `val`.toFloat()
        } catch (exception: ArgumentIndexException) {
            return 0F
        } catch (exception: NumberFormatException) {
            return 0F
        }
    }

    fun getArgAsPlayer(index: Int): Player? {
        try {
            val `val` = getArgAsString(index)
            val player = Bukkit.getPlayerExact(`val`)
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
            return null
        }
    }
}