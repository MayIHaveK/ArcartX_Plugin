/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.server

import com.google.gson.annotations.SerializedName
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.nms.ItemBridge

class SPackSlotItemStack : ServerPacket {

    @SerializedName("id")
    private var id: String

    @SerializedName("jsonItem")
    private var jsonItem: String? = null

    @SerializedName("isStartWith")
    private var isStartWith: Boolean = false

    @SerializedName("isDelete")
    private var delete: Boolean = false

    constructor(id: String, itemStack: ItemStack) {
        this.id = id
        this.jsonItem = ItemBridge.item2json(itemStack)
    }


    constructor(id: String, isStartWith: Boolean, delete: Boolean) {
        this.id = id
        this.isStartWith = isStartWith
        this.delete = delete
    }

}