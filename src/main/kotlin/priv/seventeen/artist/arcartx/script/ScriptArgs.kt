/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.script

/** 脚本参数解析器 */
class ScriptArgs (private val args: Map<String,String>){

    fun getString(key: String) : String {
        return args[key] ?: ""
    }

    fun getInt(key: String) : Int {
        return args[key]?.toIntOrNull() ?: 0
    }

    fun getDouble(key: String) : Double {
        return args[key]?.toDoubleOrNull() ?: 0.0
    }

    fun getFloat(key: String) : Float {
        return args[key]?.toFloatOrNull() ?: 0.0f
    }

    fun getLong(key: String) : Long {
        return args[key]?.toLongOrNull() ?: 0L
    }

    fun getBoolean(key: String) : Boolean {
        return args[key]?.toBoolean() ?: false
    }

    companion object {

        fun parseConfig(input: String): Pair<String, ScriptArgs> {
            // 这里不用正则
            if(input.endsWith("}")){
                val id = input.substringBefore("{").trim()
                val propertiesStr = input.substringAfter("{").substringBefore("}")
                val properties = mutableMapOf<String, String>()
                val propertiesArray = propertiesStr.split(";")
                propertiesArray.forEach {
                    val key = it.substringBefore("=").trim()
                    val value = it.substringAfter("=").trim()
                    properties[key] = value
                }
                return id to ScriptArgs(properties)
            }
            return input.trim() to ScriptArgs(emptyMap())
        }
    }
}

