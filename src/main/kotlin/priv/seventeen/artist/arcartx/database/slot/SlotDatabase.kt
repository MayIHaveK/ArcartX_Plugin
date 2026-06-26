/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.database.slot

import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.nms.ItemBridge
import java.util.*

/**
 * 槽位数据库接口。
 */
interface SlotDatabase {

    /** 仅执行查询，返回原始 JSON 串。不触碰 NMS，可在异步线程调用。 */
    fun loadPlayerSlotDataRaw(player: UUID, slots: List<String>): Map<String, String>

    /** 写入已序列化的 JSON 串。不触碰 NMS，可在异步线程调用。 */
    fun setSlotDataJson(player: UUID, slotName: String, itemJson: String)

    fun close()

    /** 查询并反序列化为 ItemStack（含 NMS，须主线程调用）。 */
    fun loadPlayerSlotData(player: UUID, slots: List<String>): Map<String, ItemStack> {
        val result = HashMap<String, ItemStack>()
        loadPlayerSlotDataRaw(player, slots).forEach { (slot, json) ->
            deserializeItemStack(json)?.let { result[slot] = it }
        }
        return result
    }

    /** 序列化（含 NMS，须主线程调用）后写入。 */
    fun setSlotData(player: UUID, slotName: String, itemStack: ItemStack) {
        setSlotDataJson(player, slotName, serializeItemStack(itemStack))
    }

    // 写入经 PreparedStatement 参数化（setString），SQL 转义由 JDBC 负责，无需手动转义。
    fun serializeItemStack(itemStack: ItemStack?): String {
        return itemStack?.let { ItemBridge.item2json(it) } ?: ""
    }

    fun deserializeItemStack(jsonSrc: String?): ItemStack? {
        if (jsonSrc.isNullOrEmpty()) return null
        return ItemBridge.json2Item(jsonSrc)
    }

}
