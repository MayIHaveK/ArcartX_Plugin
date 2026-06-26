/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.event.client

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import priv.seventeen.artist.arcartx.event.ArcartXEvent
import priv.seventeen.artist.arcartx.commons.command.exception.ArgumentIndexException
import java.util.*

/**
 * 自定义数据包事件。
 *
 * 取参约定：
 * - [getArg] / 数值类 [getArgAsInt]/[getArgAsDouble]/[getArgAsLong]/[getArgAsFloat]/[getArgAsBoolean]：
 *   下标越界或解析失败时返回**默认值**（""/0/0.0/false），不抛异常，便于脚本直接取用；
 * - [getArgAsUUID]：无合理默认值，越界或格式错误时返回 **null**。
 */
open class ClientCustomPacketEvent(val player: Player, val id: String, val data: List<String>, val argSize : Int = data.size) : ArcartXEvent(allowCancelled = false) {

    fun getArg(index: Int): String {
        return try {
            getArgAsString(index)
        } catch (e: ArgumentIndexException) {
            ""
        }
    }

    @Throws(ArgumentIndexException::class)
    protected fun getArgAsString(index: Int): String {
        try {
            return data[index]
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


    fun getArgAsUUID(index: Int): UUID? {
        try {
            val `val` = getArgAsString(index)
            return UUID.fromString(`val`)
        } catch (exception: Exception) {
            return null
        }
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers() = handlerList

}
