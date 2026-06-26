/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs.hitbox

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicConfig
import io.lumine.mythic.api.mobs.MythicMob
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.compatibility.AbstractModelEngineSupport
import io.lumine.mythic.core.mobs.model.MobModel
import org.bukkit.entity.Entity
import org.bukkit.util.BoundingBox
import java.util.*

class WrappedHitboxSupport: AbstractModelEngineSupport(MythicBukkit.inst()) {

    companion object {
        fun setup(){
            MythicBukkit.inst().compatibility.modelEngine = Optional.of(WrappedHitboxSupport())
        }
    }

    private val megSupport = this.plugin.compatibility.modelEngine.get()


    override fun load(mythicBukkit: MythicBukkit) {
    }

    override fun unload() {
    }

    override fun isSubHitbox(uuid: UUID): Boolean {
        if(InteractionProxyManager.isProxy(uuid)){
            return true
        }
        return megSupport.isSubHitbox(uuid)
    }

    override fun isBoundToSubHitbox(p0: UUID?, p1: UUID?): Boolean {
        return megSupport.isBoundToSubHitbox(p0, p1)
    }

    override fun getParentUUID(uuid: UUID): UUID {
        InteractionProxyManager.getOwner(uuid)?.let { owner ->
            return owner.uniqueId
        }
        return megSupport.getParentUUID(uuid)
    }

    override fun getParent(entity: AbstractEntity): AbstractEntity {
        val parent: Entity? = InteractionProxyManager.getOwner(entity.uniqueId)
        if (parent != null) {
            return BukkitAdapter.adapt(parent)
        }
        return megSupport.getParent(entity)
    }

    override fun overlapsOOBB(p0: BoundingBox?, p1: AbstractEntity?): Boolean {
        return megSupport.overlapsOOBB(p0, p1)
    }

    override fun getBoneModel(p0: String?, p1: String?): ModelConfig {
        return megSupport.getBoneModel(p0, p1)
    }

    override fun createMobModel(p0: MythicMob?, p1: MythicConfig?): MobModel {
        return megSupport.createMobModel(p0, p1)
    }

    override fun queuePostModelRegistration(p0: Runnable?) {
        return megSupport.queuePostModelRegistration(p0)
    }

    override fun createMobModel(entity: AbstractEntity?, modelId: String?): MobModel? {
        return megSupport.createMobModel(entity, modelId)
    }

    override fun distanceSquaredToSubHitbox(origin: AbstractLocation, entity: AbstractEntity): Double {
        return megSupport.distanceSquaredToSubHitbox(origin, entity)
    }
}