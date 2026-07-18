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

class SPackPlayerLook(
@SerializedName("yaw")
private val target: Float,
/** 相机平滑跟随强度（每 1/60s 向目标闭合比例 0-1，仅纯自由相机模式生效）；0=玩家转向但相机不跟随。 */
@SerializedName("strength")
private val strength: Float = 0f
) : ServerPacket {

}