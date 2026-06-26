/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.effect

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.effect.data.EffectPosition
import priv.seventeen.artist.arcartx.core.effect.data.WorldTextureBuilder
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.arcartx.util.EntityUtils.doWithSeenBy

/**
 * 特效管理器。
 *
 * 下列方法为 `World` / `Entity` 的 Kotlin 扩展函数，且均为 `@JvmStatic`：
 * - Kotlin：`entity.spawnModelEffect(...)`、`world.spawnWorldTextureEffect(location, ...)`
 * - Java：作为静态方法调用，接收者作为第一个参数——
 *   `ArcartXEffectManager.spawnModelEffect(entity, ...)`、
 *   `ArcartXEffectManager.spawnWorldTextureEffect(world, location, ...)`
 */
object ArcartXEffectManager {

    @JvmStatic
    fun World.spawnModelEffect(location: Location, modelID: String, modelScale: Float, keepTime: Int, glow: Boolean){
        this.getNearbyEntities(location,128.0,128.0,128.0).filterIsInstance<Player>().parallelStream().forEach {
            NetworkMessageSender.sendModelEffect(it, modelID, modelScale,keepTime, glow, EffectPosition.location(location))
        }
    }

    @JvmStatic
    fun Entity.spawnModelEffect(modelID: String, modelScale: Float, keepTime: Int, glow: Boolean, effectPosition: EffectPosition){
        this.doWithSeenBy{
            NetworkMessageSender.sendModelEffect(it, modelID, modelScale, keepTime, glow, effectPosition)
        }
    }

    @JvmStatic
    fun World.spawnModelEffect(identifier: String, location: Location, modelID: String, animation: String, speed: Double, modelScale: Float, keepTime: Int, glow: Boolean){
        this.getNearbyEntities(location,128.0,128.0,128.0).filterIsInstance<Player>().parallelStream().forEach {
            NetworkMessageSender.sendModelEffect(it,identifier, modelID, animation,speed, modelScale,keepTime, glow, EffectPosition.location(location))
        }
    }

    @JvmStatic
    fun World.removeModelEffect(identifier: String,location: Location){
        this.getNearbyEntities(location,128.0,128.0,128.0).filterIsInstance<Player>().parallelStream().forEach {
            NetworkMessageSender.sendRemoveModelEffect(it,identifier)
        }
    }

    @JvmStatic
    fun Entity.spawnModelEffect(identifier: String,modelID: String, animation: String, speed: Double, modelScale: Float, keepTime: Int, glow: Boolean, effectPosition: EffectPosition){
        this.doWithSeenBy{
            NetworkMessageSender.sendModelEffect(it,identifier, modelID,animation,speed, modelScale, keepTime, glow, effectPosition)
        }
    }

    @JvmStatic
    fun Entity.removeModelEffect(identifier: String){
        this.doWithSeenBy{
            NetworkMessageSender.sendRemoveModelEffect(it,identifier)
        }
    }

    @JvmStatic
    fun World.spawnWorldTextureEffect(location: Location, identifier: String, worldTexture: WorldTextureBuilder, effectPosition: EffectPosition){
        this.getNearbyEntities(location,128.0,128.0,128.0).filterIsInstance<Player>().parallelStream().forEach {
            NetworkMessageSender.sendWorldTexture(it, identifier, worldTexture, effectPosition)
        }
    }

    @JvmStatic
    fun Entity.spawnWorldTextureEffect(identifier: String, worldTexture: WorldTextureBuilder, effectPosition: EffectPosition){
        this.doWithSeenBy{
            NetworkMessageSender.sendWorldTexture(it, identifier, worldTexture, effectPosition)
        }
    }


    @JvmStatic
    fun Entity.spawnBedrockParticle(particleID: String, effectPosition: EffectPosition){
        this.doWithSeenBy{
            NetworkMessageSender.sendBedrockParticle(it,  particleID, effectPosition)
        }
    }

    @JvmStatic
    fun World.spawnBedrockParticle(location: Location,particleID: String, effectPosition: EffectPosition){
        this.getNearbyEntities(location,128.0,128.0,128.0).filterIsInstance<Player>().parallelStream().forEach {
            NetworkMessageSender.sendBedrockParticle(it, particleID, effectPosition)
        }
    }





}