/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.core.entity.data.ArcartXPlayer
import priv.seventeen.artist.asteroid.util.FakeOp

/** 玩家工具类 */
object PlayerUtils {

    @JvmStatic
    fun Player.isNpc() : Boolean {
        return this.hasMetadata("NPC")
    }

    @JvmStatic
    val Player.arcartXHandler: ArcartXPlayer?
        get() = ArcartXEntityManager.getPlayer(this)


    @JvmStatic
    fun Player.dispatchOpCommand(command: String){
        FakeOp.execute(this, command)
    }


}