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

class SPackPlayEntitySound(
    @SerializedName("path")
    private val path: String,
    @SerializedName("entity")
    private val entity: String,
    @SerializedName("soundCategory")
    private val soundCategory: String,
    @SerializedName("distOrRoll")
    private val distOrRoll: Int,
    @SerializedName("pitch")
    private val pitch: Double,
    @SerializedName("keepTime")
    private val keepTime: Int,
    @SerializedName("name")
    private val name: String?
) : ServerPacket {


}