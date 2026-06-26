/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.client

import com.google.gson.annotations.SerializedName
import org.bukkit.entity.Player
import java.util.*

/** 骨骼命中包（占位，检测逻辑待实现） */
class CPackHitBone : ClientPacket {
    // TODO 骨骼命中检测尚未实现，当前为占位包

    @SerializedName("uuid")
    private var uuid: UUID? = null

    @SerializedName("bone")
    private var bone: String? = null

    override fun handle(player: Player) {
        // 预留：骨骼命中逻辑
    }

    override val isAsync: Boolean
        get() = false
}
