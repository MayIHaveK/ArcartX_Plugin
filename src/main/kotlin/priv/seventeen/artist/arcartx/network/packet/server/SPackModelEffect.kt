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
import priv.seventeen.artist.arcartx.core.effect.data.EffectPosition

class SPackModelEffect(
    @SerializedName("modelID")
    private var modelID: String? = null,
    @SerializedName("scale")
    private val scale: Float = 0f,
    @SerializedName("keepTime")
    private val keepTime: Int = 0,
    @SerializedName("glow")
    private val glow: Boolean = false,
    @SerializedName("position")
    private val position: EffectPosition
) : ServerPacket {

}