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

class SPackScreenCommand(
    @SerializedName("cmd")
    private val cmd: String,
    @SerializedName("id")
    private val id: String,
    @SerializedName("code")
    private val code: String = ""
) : ServerPacket {


}