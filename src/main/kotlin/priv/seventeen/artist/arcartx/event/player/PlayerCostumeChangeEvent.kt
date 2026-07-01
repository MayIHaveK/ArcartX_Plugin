/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.event.player

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import priv.seventeen.artist.arcartx.core.entity.data.ArcartXPlayer
import priv.seventeen.artist.arcartx.event.ArcartXEvent

/**
 * 玩家时装变更事件(变更前触发，可取消)。
 * - EQUIP_SUIT：穿全套装，slot=null，modelID=套装模型，hide。
 * - EQUIP_SLOT：穿某槽，slot=该槽，modelID=模型，hide。
 * - REMOVE_SLOT：脱某槽，slot=该槽，modelID=null。
 * - CLEAR：脱下全部，slot=null，modelID=null。
 */
class PlayerCostumeChangeEvent(
    val player: Player,
    val type: Type,
    val slot: ArcartXPlayer.CostumeSlot?,
    val modelID: String?,
    val hide: Boolean
) : ArcartXEvent() {

    enum class Type { EQUIP_SUIT, EQUIP_SLOT, REMOVE_SLOT, CLEAR }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers() = handlerList
}
