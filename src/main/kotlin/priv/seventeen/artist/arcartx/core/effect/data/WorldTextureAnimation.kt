/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.effect.data

import com.google.gson.annotations.SerializedName

class WorldTextureAnimation(
    @SerializedName("type")
    val type: String,
    @SerializedName("delay")
    val delay: Int = 0,
    @SerializedName("from")
    val from: Double,
    @SerializedName("to")
    val to: Double,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("loop")
    val loop: Boolean
) {
}