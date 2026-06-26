/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.link.papi

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.PlaceholderHook
import org.bukkit.OfflinePlayer

/** PAPI 变量注册钩子 */
class PlaceholderHooker {

    fun register(placeholder: AXPlaceholder){
        PlaceholderAPI.registerPlaceholderHook(placeholder.identifier, object : PlaceholderHook() {
            override fun onRequest(p: OfflinePlayer, params: String): String {
                return placeholder.invokePlaceholderFunction(p, params)
            }
        })
    }


    fun unregister(placeholder: AXPlaceholder){
        PlaceholderAPI.unregisterPlaceholderHook(placeholder.identifier)
    }


}