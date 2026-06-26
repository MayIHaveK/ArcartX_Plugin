/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs

import io.lumine.mythic.bukkit.events.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox.InteractionProxyManager
import priv.seventeen.artist.arcartx.hook.mythicmobs.mechanic.*
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler.runTaskLater
import priv.seventeen.artist.blink.bukkitPlugin
import java.util.*

/** MythicMobs 事件监听器 */
class MythicMobsListener : Listener {

    private val cfg = MythicMobsHooker.config
        ?: error("MythicMobs 关键字配置未加载，无法初始化 MythicMobsListener")

    private val model: String = cfg.model.lowercase(Locale.ROOT)
    private val animation: String = cfg.animation.lowercase(Locale.ROOT)
    private val hitbox: String = cfg.hitbox.lowercase(Locale.ROOT)
    private val defaultState: String = cfg.defaultState.lowercase(Locale.ROOT)
    private val hideBone: String = cfg.hideBone.lowercase(Locale.ROOT)
    private val sound: String = cfg.sound.lowercase(Locale.ROOT)
    private val bedrockParticle: String = cfg.bedrockParticle.lowercase(Locale.ROOT)
    private val hammer: String = cfg.hammerEffect.lowercase(Locale.ROOT)
    private val uiPacket: String = cfg.uiPack.lowercase(Locale.ROOT)
    private val hitboxProxy: String = cfg.hitboxProxy.lowercase(Locale.ROOT)
    private val cameraShake: String = cfg.cameraShake.lowercase(Locale.ROOT)
    private val proxyMountType: String = cfg.proxyMountType.lowercase(Locale.ROOT)
    private val proxyMountSpeed: String = cfg.proxyMountSpeed.lowercase(Locale.ROOT)
    private val proxyAddSeat: String = cfg.proxyAddSeat.lowercase(Locale.ROOT)

    private val mechanicFactories = mapOf(
        model to ::Model,
        animation to ::Animation,
        hitbox to ::BoundingBox,
        defaultState to ::DefaultState,
        hideBone to ::HideBone,
        sound to ::Sound,
        bedrockParticle to ::BedrockParticle,
        hammer to ::HammerCrack,
        uiPacket to ::UIPacket,
        hitboxProxy to ::HitboxProxy,
        cameraShake to ::CameraShake,
        proxyAddSeat to ::ProxyAddSeat,
        proxyMountType to ::ProxyMountType,
        proxyMountSpeed to ::ProxyMountSpeed,
    )

    @EventHandler
    fun onMythicMechanicLoad(event: MythicMechanicLoadEvent) {
        val name = event.mechanicName.lowercase()
        mechanicFactories[name]?.let { factory ->
            event.register(factory(event.container, event.config))
        }
    }


    @EventHandler
    fun onMobSpawn(event: MythicMobSpawnEvent) {
        val config = event.mobType.config
        val hideName = config.getBoolean("HideName", false)
        val model = config.getString("Model", null)
        val scale = config.getDouble("Scale", 1.0)
        val hideBox = config.getBoolean("HideHitBox", false)
        val width = config.getDouble("Width", -1.0)
        val height = config.getDouble("Height", -1.0)
        val proxyWidth = config.getDouble("ProxyWidth", -1.0)
        val proxyHeight = config.getDouble("ProxyHeight", -1.0)

        // 仅在确有属性需要时创建一次实体，避免重复 getOrCreateEntity
        if (model != null || hideName || hideBox || (width > 0 && height > 0)) {
            val entity = ArcartXEntityManager.getOrCreateEntity(event.entity)
            if (model != null) entity?.setModel(model, scale)
            if (hideName) entity?.setDisplayName(false)
            if (hideBox) entity?.setHideHitBox(true)
            if (width > 0 && height > 0) entity?.setSize(width, height)
        }

        if (proxyWidth > 0 && proxyHeight > 0) {
            bukkitPlugin.runTaskLater({
                InteractionProxyManager.createProxy(event.entity, proxyWidth, proxyHeight)
            }, 1L)
        }
    }

    @EventHandler
    fun onMobDespawn(event: MythicMobDespawnEvent) {
        ArcartXEntityManager.removeEntity(event.entity)
        // 移除碰撞箱代理
        InteractionProxyManager.removeProxy(event.entity)
    }

    @EventHandler
    fun onMobDeath(event: MythicMobDeathEvent) {
        ArcartXEntityManager.removeEntity(event.entity)
        // 移除碰撞箱代理
        InteractionProxyManager.removeProxy(event.entity)
    }

    @EventHandler
    fun onMythicProjectileHitEvent(event: MythicProjectileHitEvent) {
        if(event.projectile.data.caster.entity == event.entity && InteractionProxyManager.isOwner(event.entity.uniqueId)){
            event.isCancelled = true
        }
    }

}