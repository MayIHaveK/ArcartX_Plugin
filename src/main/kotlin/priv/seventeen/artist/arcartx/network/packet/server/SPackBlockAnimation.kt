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

class SPackBlockAnimation(
    @SerializedName("x")
    private val x: Int,
    @SerializedName("y")
    private val y: Int,
    @SerializedName("z")
    private val z: Int,
    @SerializedName("animation")
    private val animation: String,
    @SerializedName("speed")
    private val speed:Double,
    @SerializedName("transitionTime")
    private val transitionTime: Int,
    @SerializedName("keepTime")
    private val keepTime: Long
) : ServerPacket {

}