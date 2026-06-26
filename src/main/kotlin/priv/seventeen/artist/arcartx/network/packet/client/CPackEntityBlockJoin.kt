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
import org.bukkit.block.Skull
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import priv.seventeen.artist.arcartx.blockNamespace
import priv.seventeen.artist.arcartx.network.NetworkMessageSender

/** 方块模型加载包 */
class CPackEntityBlockJoin : ClientPacket {

    @SerializedName("x")
    var x: Int = 0

    @SerializedName("y")
    var y: Int = 0

    @SerializedName("z")
    var z: Int = 0


    override fun handle(player: Player) {
        val state = player.world.getBlockState(x, y, z)
        if(state is Skull && state.hasOwner() && state.owner.equals("_arcartx_model_")){
            if(state.persistentDataContainer.has(blockNamespace)){
                val model = state.persistentDataContainer.get(blockNamespace, PersistentDataType.STRING) ?: return
                NetworkMessageSender.sendBlockModel(player,x,y,z,model)
            }
        }
    }

    override val isAsync: Boolean
        get() = false
}