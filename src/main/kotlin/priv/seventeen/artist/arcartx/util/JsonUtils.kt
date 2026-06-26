/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

import com.google.gson.Gson

/** JSON 序列化工具 */
object JsonUtils {

    private val gson: Gson = Gson()


    @JvmStatic
    fun Any.toJson(): String {
        return gson.toJson(this)
    }

    @JvmStatic
    fun <T> String.fromJson(clazz: Class<T>): T {
        return gson.fromJson(this, clazz)
    }
}