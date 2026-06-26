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
import priv.seventeen.artist.arcartx.core.config.ui.type.UI
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.blink.bukkitPlugin

/** UI 编辑器数据包 */
class CPackUIData : ClientPacket {

    @SerializedName("data")
    var data: UI? = null

    override fun handle(player: Player) {
        if(!player.isOp){
            player.kickPlayer(L(AXLanguageKey.KICK_ILLEGAL_PACKET))
        }
        data?.let {
            it.plugin = bukkitPlugin
            it.pathName = "ui_editor_output/${it.id}.yml"
            it.load()
            player.closeInventory()
        }
    }

    override val isAsync: Boolean
        get() = false


}