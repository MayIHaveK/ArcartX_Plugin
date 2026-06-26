/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.adyeshach

import ink.ptms.adyeshach.core.Adyeshach
import ink.ptms.adyeshach.core.event.AdyeshachPersistentTagUpdateEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import priv.seventeen.artist.arcartx.event.client.ClientEntityJoinEvent
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import java.util.*

/** Adyeshach 事件监听器 */
class AdyeshachListener : Listener {


    @EventHandler
    fun onAdyeshachTagUpdateEvent(event: AdyeshachPersistentTagUpdateEvent) {
        if(event.key.startsWith(TAG_PREFIX)){
            val entity = event.entity
            val value = event.value
            val type: String = event.key.substring(TAG_PREFIX.length)
            val uuid = UUID.fromString(entity.uniqueId)
            when (type) {
                "Model" -> {
                    if(value == null){
                        entity.getVisiblePlayers().forEach {  NetworkMessageSender.setEntityModel(it, uuid, "", 1.0) }
                        return
                    }
                    if(entity.hasPersistentTag("ArcartX_Scale")){
                        val scale = entity.getPersistentTag("ArcartX_Scale")?: "1"
                        entity.getVisiblePlayers().forEach { NetworkMessageSender.setEntityModel(it, uuid, value, scale.toDouble())  }
                    }
                    return
                }
                "Scale" -> {
                    val scale = value?.toDouble() ?: 1.0
                    if(entity.hasPersistentTag("ArcartX_Model")){
                        val model = entity.getPersistentTag("ArcartX_Model")
                        if(model == null){
                            entity.getVisiblePlayers().forEach {  NetworkMessageSender.setEntityModel(it, uuid, "", 1.0) }
                            return
                        }
                        entity.getVisiblePlayers().forEach { NetworkMessageSender.setEntityModel(it, uuid, model, scale)  }
                    }
                    return
                }
                "Width" -> {
                    if(value == null){
                        entity.getVisiblePlayers().forEach { NetworkMessageSender.sendEntitySize(it, uuid, -1.0, -1.0)  }
                        return
                    }
                    if(entity.hasPersistentTag("ArcartX_Height")){
                        val height = entity.getPersistentTag("ArcartX_Height")?: "-1"
                        entity.getVisiblePlayers().forEach { NetworkMessageSender.sendEntitySize(it, uuid, value.toDouble(), height.toDouble())  }
                    }
                }
                "Height" -> {
                    if(value == null){
                        entity.getVisiblePlayers().forEach { NetworkMessageSender.sendEntitySize(it, uuid, -1.0, -1.0)  }
                        return
                    }
                    if(entity.hasPersistentTag("ArcartX_Width")){
                        val width = entity.getPersistentTag("ArcartX_Width")?: "-1"
                        entity.getVisiblePlayers().forEach { NetworkMessageSender.sendEntitySize(it, uuid, width.toDouble(), value.toDouble())  }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onEntityJoinPlayerWorld(event : ClientEntityJoinEvent){
        Adyeshach.api().getEntityFinder().getEntityFromUniqueId(event.entityUUID.toString(), event.player)?.let {
            val model = it.getPersistentTag("ArcartX_Model")
            val scale = it.getPersistentTag("ArcartX_Scale")
            val width = it.getPersistentTag("ArcartX_Width")
            val height = it.getPersistentTag("ArcartX_Height")
            if(model != null && scale != null){
                NetworkMessageSender.setEntityModel(event.player, event.entityUUID, model, scale.toDouble())
            }
            if(width != null && height != null){
                NetworkMessageSender.sendEntitySize(event.player, event.entityUUID, width.toDouble(), height.toDouble())
            }

            NetworkMessageSender.sendAdyeshachJoin(event.player,UUID.fromString(it.uniqueId))
        }
    }

    companion object {
        /** ArcartX 持久化标签的统一前缀（substring 时按其长度截取，避免魔法数 8） */
        private const val TAG_PREFIX = "ArcartX_"
    }

}