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

class SPackSetState(
    @SerializedName("uuid")
    private val uuid: UUID,
    @SerializedName("controller")
    private val controller: String,
    @SerializedName("state")
    private val state: String,
    @SerializedName("speed")
    private val speed: Double,
    @SerializedName("moveBreak")
    private val moveBreak: Long,
    /** 是否要求"新的"移动输入（取消窗口开启后先松开再按）才触发移动取消，缺省 false=按住即取消 */
    @SerializedName("moveBreakFresh")
    private val moveBreakFresh: Boolean = false
) : ServerPacket {

}