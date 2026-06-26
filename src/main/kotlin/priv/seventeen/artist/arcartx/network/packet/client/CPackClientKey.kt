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
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.event.client.ClientKeyPressEvent
import priv.seventeen.artist.arcartx.event.client.ClientKeyReleaseEvent

/** 客户端按键包 */
class CPackClientKey: ClientPacket {

    @SerializedName("keyID")
    private var keyID: String? = null

    @SerializedName("isDown")
    private var isDown = false

    override fun handle(player: Player) {
        val key = keyID ?: return
        if (isDown) {
            ClientKeyPressEvent(player, key).call()
            ArcartX.configs.clientKeyFolder.elementMap[key]?.callBack?.onPress(player)
        } else {
            ClientKeyReleaseEvent(player, key).call()
            ArcartX.configs.clientKeyFolder.elementMap[key]?.callBack?.onRelease(player)
        }
    }

    override val isAsync: Boolean
        get() = false
}