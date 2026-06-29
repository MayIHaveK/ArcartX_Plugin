/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.entity

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.entity.data.ArcartXEntity
import priv.seventeen.artist.arcartx.core.entity.data.ArcartXPlayer
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler
import priv.seventeen.artist.arcartx.util.EntityUtils.doWithSeenBy
import priv.seventeen.artist.arcartx.util.PlayerUtils.isNpc
import priv.seventeen.artist.blink.bukkitPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/** 实体管理器 */
object ArcartXEntityManager {

    init{
        AsteroidScheduler.runTaskTimer(bukkitPlugin, Runnable {
            _entities.values.removeIf(ArcartXEntity::isExpired)
        }, 0L, ArcartX.configs.setting.entityCacheClearInterval * 1L)
        AsteroidScheduler.runTaskTimer(bukkitPlugin, Runnable {
            _players.values.forEach(ArcartXPlayer::updateFlyingState)
        }, 0L, 1L)
    }

    private val _entities: MutableMap<UUID, ArcartXEntity> = ConcurrentHashMap()
    private val _players: MutableMap<UUID, ArcartXPlayer> = ConcurrentHashMap()


    val entities: Map<UUID, ArcartXEntity> = Collections.unmodifiableMap(_entities)
    val players: Map<UUID, ArcartXPlayer> = Collections.unmodifiableMap(_players)


    fun getPlayer(player: Player) : ArcartXPlayer ? {
        if(player.isNpc()){
            return null
        }
        return _players.computeIfAbsent(player.uniqueId) { ArcartXPlayer(player) }
    }

    fun removePlayer(player: Player){
        _players.remove(player.uniqueId)
        player.doWithSeenBy {
            NetworkMessageSender.sendSetController(it,player.uniqueId , "")
        }
    }

    fun getOrCreateEntity(entity: Entity) : ArcartXEntity ? {
        if(entity is Player && !entity.isNpc()){
            return getPlayer(entity)
        }
        return _entities.computeIfAbsent(entity.uniqueId) { ArcartXEntity(entity) }
    }

    fun getEntity(entity: UUID) : ArcartXEntity ? {
        _players[entity]?.let {
            return it
        }
        return _entities[entity]
    }

    fun removeEntity(entity: Entity){
        if(entity is Player) return
        _entities.remove(entity.uniqueId)
    }


}