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
import java.util.*


class SPackCostume(
    @SerializedName("uuid")
    private val target: UUID,
    @SerializedName("suit")
    private val suit: String?,
    @SerializedName("suitHide")
    private val suitHide: Boolean,
    @SerializedName("slots")
    private val slots: Map<String, Slot>
) : ServerPacket {

    class Slot(
        @SerializedName("model")
        private val model: String,
        @SerializedName("hide")
        private val hide: Boolean
    )
}
