/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.client

import com.google.gson.annotations.SerializedName
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import java.util.function.Consumer

/** PAPI 变量请求包 */
class CPackRequestPlaceholder : ClientPacket {

    @SerializedName("placeholders")
    var placeholders: List<String>? = null


    override fun handle(player: Player) {
        val list = placeholders ?: return
        list.forEach(Consumer<String> { key: String ->
            var flag = true
            for (s in ArcartX.configs.setting.papiBlacklist) {
                if (key.contains(s)) {
                    flag = false
                    break
                }
            }
            if (flag) {
                var papi = key
                if (!key.startsWith("%")) {
                    papi = "%$key%"
                }
                val result: String = PlaceholderAPI.setPlaceholders(player, papi)
                if (!result.startsWith("%")) {
                    NetworkMessageSender.sendClientPlaceholder(
                        player,
                        key,
                        result
                    )
                }
            }
        })
    }
}