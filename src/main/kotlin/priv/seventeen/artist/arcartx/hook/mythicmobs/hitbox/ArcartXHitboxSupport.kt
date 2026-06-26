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
import io.lumine.mythic.api.config.MythicConfig
import io.lumine.mythic.api.mobs.MythicMob
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.compatibility.AbstractModelEngineSupport
import io.lumine.mythic.core.mobs.MobType
import io.lumine.mythic.core.mobs.model.MobModel
import org.bukkit.entity.Entity
import org.bukkit.util.BoundingBox
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler.runTaskLater
import priv.seventeen.artist.blink.bukkitPlugin
import java.util.*

/** ArcartX 碰撞箱支持，替代 ModelEngine 的碰撞箱功能 */
class ArcartXHitboxSupport : AbstractModelEngineSupport(MythicBukkit.inst()) {

    companion object {
        fun setup(){
            MythicBukkit.inst().compatibility.modelEngine = Optional.of(ArcartXHitboxSupport())
        }
    }

    override fun isSubHitbox(uuid: UUID): Boolean {
        return InteractionProxyManager.isProxy(uuid)
    }

    override fun getParent(entity: AbstractEntity): AbstractEntity {
        val parent: Entity? = InteractionProxyManager.getOwner(entity.uniqueId)
        if (parent != null) {
            return BukkitAdapter.adapt(parent)
        }
        return entity
    }

    override fun getParentUUID(uuid: UUID): UUID {
        InteractionProxyManager.getOwner(uuid)?.let { owner ->
            return owner.uniqueId
        }
        return uuid
    }

    override fun isBoundToSubHitbox(subHitbox: UUID?, bound: UUID?): Boolean {
        if (subHitbox == null || bound == null) return false
        val owner = InteractionProxyManager.getOwner(subHitbox) ?: return false
        return owner.uniqueId == bound
    }



    override fun overlapsOOBB(box: BoundingBox?, entity: AbstractEntity?): Boolean {
        return false
    }



    @Throws(IllegalArgumentException::class)
    override fun getBoneModel(modelId: String?, boneId: String?): ModelConfig {
        throw IllegalArgumentException("ModelEngine is not installed")
    }

    override fun createMobModel(mob: MythicMob, config: MythicConfig): MobModel? {
        var applyInvisibility = config.getBoolean("Options.ApplyInvisibility", false)
        applyInvisibility = config.getBoolean("Options.Invisibility", applyInvisibility)
        applyInvisibility = config.getBoolean("Options.Invisible", applyInvisibility)
        if(!applyInvisibility) {
            bukkitPlugin.runTaskLater({
                if(mob is MobType) {
                    Accessor.setApplyInvisibility(mob, applyInvisibility)
                }
            }, 1L)
        }
        return null
    }


    override fun queuePostModelRegistration(runnable: Runnable) {
        runnable.run()
    }

    override fun load(plugin: MythicBukkit?) {
    }

    override fun unload() {
    }


}