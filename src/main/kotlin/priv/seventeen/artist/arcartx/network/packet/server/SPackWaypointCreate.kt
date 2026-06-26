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

class SPackWaypointCreate (
    @SerializedName("id")
    private val id: String,
    @SerializedName("title")
    private val title: String,
    @SerializedName("dataId")
    private val dataId: String,
    @SerializedName("x")
    private val x: Double,
    @SerializedName("y")
    private val y: Double,
    @SerializedName("z")
    private val z: Double,
) : ServerPacket {

}