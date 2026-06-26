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

class SPackEntityDefaultState(
    @SerializedName("uuid")
    private val uuid: UUID,
    @SerializedName("name")
    private val name: String,
    @SerializedName("state")
    private val state: String
) : ServerPacket {

}