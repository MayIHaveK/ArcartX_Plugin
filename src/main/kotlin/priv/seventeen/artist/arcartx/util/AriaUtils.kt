/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.aria.Aria
import priv.seventeen.artist.aria.api.AriaCompiledRoutine
import priv.seventeen.artist.blink.bukkitPlugin

/** Aria 工具类 */
class AriaUtils {

    fun parser(name: String, code: String): AriaCompiledRoutine? {
        try {
            return Aria.compile(name, code + "\n")
        } catch (e: Exception) {
            try {
                return Aria.compile(name,  "false\n")
            } catch (e2: Exception) {
                // 回退编译 "false" 理论上不会失败；即便失败，下方已用原始异常 e 上报，无需重复
            }
            bukkitPlugin.sendException(name, e)
        }
        return null
    }
}