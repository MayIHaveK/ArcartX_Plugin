/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.papi

import org.bukkit.OfflinePlayer
import priv.seventeen.artist.arcartx.commons.link.papi.annotation.Placeholder
import priv.seventeen.artist.arcartx.commons.link.papi.annotation.PlaceholderArgs
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendDebug
import priv.seventeen.artist.blink.bukkitPlugin
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles

/** PAPI 变量处理基类 */
abstract class AXPlaceholder {

    private val functions: MutableMap<String, MethodHandle> = HashMap()
    private val argsMap: MutableMap<String, Array<String>> = HashMap()

    init {
        val lookup = MethodHandles.lookup()
        for (method in javaClass.declaredMethods) {
            if (method.isAnnotationPresent(Placeholder::class.java)) {
                if (method.returnType != String::class.java) {
                    continue
                }
                val paramTypes = method.parameterTypes
                if (paramTypes.size != 1 || paramTypes[0] != PlaceholderContext::class.java) {
                    continue
                }
                method.isAccessible = true
                val placeholder = method.getAnnotation(
                    Placeholder::class.java
                )
                try {
                    val handle = lookup.unreflect(method)
                    functions[placeholder.value] = handle
                    if (method.isAnnotationPresent(PlaceholderArgs::class.java)) {
                        argsMap[placeholder.value] = method.getAnnotation(
                            PlaceholderArgs::class.java
                        ).value
                    }
                } catch (e: IllegalAccessException) {
                    // 反射 unreflect 失败：该 placeholder 跳过注册，调试模式记录便于排查
                    bukkitPlugin.sendDebug("placeholder ${placeholder.value} 注册失败: ${e.message}")
                }
            }
        }
    }

    fun invokePlaceholderFunction(player: OfflinePlayer?, `in`: String): String {
        val args = `in`.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val cmd = args[0]
        val handle = functions.getOrDefault(cmd, null)

        if (handle != null) {
            val placeholderContext: PlaceholderContext
            if (args.size > 1) {
                val data = arrayOfNulls<String>(args.size - 1)
                System.arraycopy(args, 1, data, 0, args.size - 1)
                placeholderContext = PlaceholderContext(player, data)
            } else {
                placeholderContext = PlaceholderContext(player, arrayOfNulls(0))
            }
            val needArgs = argsMap[cmd]
            if (needArgs != null) {
                if (args.size - 1 != needArgs.size) {
                    return "parameter error"
                }
            }
            try {
                return handle.invoke(this, placeholderContext) as String
            } catch (_: Throwable) {
                return "invoke error"
            }
        }
        return ""
    }


    abstract val identifier: String
    
}