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
import priv.seventeen.artist.arcartx.event.client.ClientSimpleKeyPressEvent
import priv.seventeen.artist.arcartx.event.client.ClientSimpleKeyReleaseEvent
import priv.seventeen.artist.arcartx.script.ScriptManager

/** 简单按键按下/释放包 */
class CPackSimpleKeyPress : ClientPacket {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("isDown")
    var isDown: Boolean = false

    override fun handle(player: Player) {
        val keyName = name ?: return
        if (!isDown) {
            ClientSimpleKeyReleaseEvent(player, keyName).call()
            ArcartX.configs.simpleKeyFolder.element[keyName]?.let {
                it.callBack?.onRelease(player)
                it.triggerScript?.forEach { script ->
                    ScriptManager.executeScript(script.first, player, script.second)
                }
            }
        } else {
            ClientSimpleKeyPressEvent(player, keyName).call()
            ArcartX.configs.simpleKeyFolder.element[keyName]?.callBack?.onPress(player)
        }
    }

    override val isAsync: Boolean
        get() = false
}