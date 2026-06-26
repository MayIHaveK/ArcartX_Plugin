/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.core.entity.data.ArcartXEntity
import priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox.InteractionProxyManager
import priv.seventeen.artist.arcartx.nms.EntityBridge
import priv.seventeen.artist.arcartx.util.collections.PlayerCallBack

/** 实体工具类 */
object EntityUtils {

    @JvmStatic
    fun Entity.setSize(width: Float, height: Float){
        EntityBridge.setEntitySize(this, width, height)
    }


    @JvmStatic
    fun Entity.doWithSeenBy(action: PlayerCallBack) {
        EntityBridge.doWithSeenBy(this) { action.call(it) }
        if(this is Player){
            action.call(this)
        }
    }

    @JvmStatic
    val Entity.arcartXHandler : ArcartXEntity?
        get() {return ArcartXEntityManager.entities[this.uniqueId]}

    @JvmStatic
    val Entity.isProxy: Boolean
        get() = InteractionProxyManager.isProxy(this)
}