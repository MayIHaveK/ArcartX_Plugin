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
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.InventoryView


class CPackContainerClick : ClientPacket {


    @SerializedName("slot")
    var slot: Int = 0

    @SerializedName("clickType")
    var clickType: String? = null


    override val isAsync: Boolean
        get() = false

    override fun handle(player: Player) {
        val view: InventoryView = player.openInventory
        val rawSlot = slot
        if (rawSlot < 0 || rawSlot >= view.countSlots()) {
            return
        }

        val click = parseClickType(clickType)
        val slotType = view.getSlotType(rawSlot)
        val action = deriveAction(click, view, rawSlot, player)

        val event = InventoryClickEvent(view, slotType, rawSlot, click, action)
        Bukkit.getPluginManager().callEvent(event)
    }

    private fun parseClickType(raw: String?): ClickType {
        val name = raw?.trim()?.uppercase() ?: return ClickType.LEFT
        // 兼容数字按钮：0=左 1=右 2=中键
        when (name) {
            "0" -> return ClickType.LEFT
            "1" -> return ClickType.RIGHT
            "2" -> return ClickType.MIDDLE
        }
        return runCatching { ClickType.valueOf(name) }.getOrDefault(ClickType.LEFT)
    }

    /** 尽量贴近原版地推断一个 InventoryAction，方便监听器判断；拿不准用 UNKNOWN(不影响 getClick/getRawSlot) */
    private fun deriveAction(click: ClickType, view: InventoryView, rawSlot: Int, player: Player): InventoryAction {
        val cursorEmpty = player.itemOnCursor.type.isAir
        val current = view.getItem(rawSlot)
        val currentEmpty = current == null || current.type.isAir
        return when (click) {
            ClickType.LEFT, ClickType.CREATIVE ->
                if (cursorEmpty) (if (currentEmpty) InventoryAction.NOTHING else InventoryAction.PICKUP_ALL)
                else InventoryAction.PLACE_ALL
            ClickType.RIGHT ->
                if (cursorEmpty) (if (currentEmpty) InventoryAction.NOTHING else InventoryAction.PICKUP_HALF)
                else InventoryAction.PLACE_ONE
            ClickType.SHIFT_LEFT, ClickType.SHIFT_RIGHT ->
                if (currentEmpty) InventoryAction.NOTHING else InventoryAction.MOVE_TO_OTHER_INVENTORY
            ClickType.MIDDLE -> InventoryAction.CLONE_STACK
            ClickType.DROP -> if (currentEmpty) InventoryAction.NOTHING else InventoryAction.DROP_ONE_SLOT
            ClickType.CONTROL_DROP -> if (currentEmpty) InventoryAction.NOTHING else InventoryAction.DROP_ALL_SLOT
            ClickType.DOUBLE_CLICK -> InventoryAction.COLLECT_TO_CURSOR
            else -> InventoryAction.UNKNOWN
        }
    }
}
