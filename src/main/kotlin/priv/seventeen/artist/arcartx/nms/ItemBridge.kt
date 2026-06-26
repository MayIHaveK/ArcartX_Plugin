/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.nms

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.asteroid.AsteroidAPI
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.asteroid.item.ItemTagData
import priv.seventeen.artist.asteroid.item.ItemTagList

/** 物品 NMS 桥接，封装 Asteroid 物品序列化与 ItemTag 操作 */
object ItemBridge {

    private val itemStackNMS by lazy { AsteroidAPI.getItemStackNMS() }

    // ==================== 物品序列化 ====================

    fun item2json(itemStack: ItemStack?): String {
        if (itemStack == null || itemStack.type.isAir) {
            return "{}"
        }
        return itemStackNMS.item2Json(itemStack)
    }

    fun json2Item(json: String): ItemStack {
        if (json.isEmpty() || json == "{}") {
            return ItemStack(Material.AIR)
        }
        return itemStackNMS.json2Item(json)
    }

    // ==================== ItemTag 扩展 ====================

    fun ItemStack.getItemTag(): ItemTag {
        return ItemTag.fromItemStack(this)
    }

    fun ItemStack.saveItemTag(tag: ItemTag): ItemStack {
        return tag.saveTo(this)
    }

    fun ItemStack.getDeepTag(path: String): ItemTagData? {
        return ItemTag.fromItemStack(this).getDeep(path)
    }

    fun ItemStack.putDeepTag(path: String, value: Any): ItemStack {
        val tag = ItemTag.fromItemStack(this)
        tag.putDeepAny(path, value)
        return tag.saveTo(this)
    }

    fun ItemStack.removeDeepTag(path: String): ItemStack {
        val tag = ItemTag.fromItemStack(this)
        tag.removeDeep(path)
        return tag.saveTo(this)
    }

    fun ItemStack.getTagList(key: String): ItemTagList? {
        val data = ItemTag.fromItemStack(this)[key] ?: return null
        return if (data is ItemTagList) data else null
    }

    fun ItemStack.editTag(block: ItemTag.() -> Unit): ItemStack {
        val tag = ItemTag.fromItemStack(this)
        tag.block()
        return tag.saveTo(this)
    }
}
