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

class SPackHammerCrackEffect(
    @SerializedName("x")
    private val x: Int,
    @SerializedName("y")
    private val y: Int,
    @SerializedName("z")
    private val z: Int,
    @SerializedName("radius")
    private val radius: Float,
    @SerializedName("depth")
    private val depth: Float,
    @SerializedName("in")
    private val `in`: Int,
    @SerializedName("keep")
    private val keep : Int,
    @SerializedName("out")
    private val out: Int,
    @SerializedName("mode")
    private val mode: Int

    ) : ServerPacket{
}