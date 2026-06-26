/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.hook.mythicmobs.bullet

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.utils.Events
import io.lumine.mythic.core.skills.SkillMechanic
import io.lumine.mythic.core.skills.projectiles.Projectile
import io.lumine.mythic.core.skills.projectiles.ProjectileBullet
import io.lumine.mythic.core.skills.projectiles.ProjectileBulletType
import io.lumine.mythic.core.skills.projectiles.ProjectileBulletableTracker
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.event.entity.EntityDamageByEntityEvent
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import java.lang.reflect.Constructor
import kotlin.math.atan2

class ModelBullet(type: ProjectileBulletType?, projectile: SkillMechanic?, config: MythicLineConfig) : ProjectileBullet(type, projectile, config) {

    private var modelID: String = config.getString(arrayOf("modelID", "mid"), "")

    private var scale: Double = config.getDouble(arrayOf("scale", "s"), 1.0)

    override fun create(projectile: ProjectileBulletableTracker, target: AbstractEntity?): BulletTracker {
        return ModelBulletTracker(projectile, target)
    }

    override fun isVirtual(): Boolean {
        return false
    }

    inner class ModelBulletTracker(projectile: ProjectileBulletableTracker?, target: AbstractEntity?) : BulletTracker(projectile, target) {

        private var bullet: AbstractEntity? = null

        init {
            Events.subscribe(EntityDamageByEntityEvent::class.java)
                .filter { this.bullet != null }
                .filter { event: EntityDamageByEntityEvent -> event.damager != null }
                .filter { event: EntityDamageByEntityEvent -> event.damager.uniqueId == bullet!!.uniqueId }
                .handler { event: EntityDamageByEntityEvent -> event.isCancelled = true }.bindWith(projectile!!)
        }

        override fun spawn(start: AbstractLocation) {
            val loc = BukkitAdapter.adapt(start)
            val currentVelocity = projectile.currentVelocity
            val delta = BukkitAdapter.adapt(currentVelocity).normalize().multiply(
                projectile.velocityMagnitude / VELOCITY_DIVISOR
            )
            val yaw = atan2(delta.x, delta.z)
            val newYaw = Math.toDegrees(-yaw).toFloat()
            loc.yaw = newYaw

            val arrow: AbstractArrow = loc.world!!.spawn(loc, Arrow::class.java)
            arrow.customName = "model:" + this@ModelBullet.modelID + "|" + this@ModelBullet.scale
            arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
            arrow.pierceLevel = ARROW_PIERCE_LEVEL
            arrow.ticksLived = Int.MAX_VALUE
            arrow.isInvulnerable = true
            arrow.setGravity(false)
            arrow.isSilent = true
            this.bullet = BukkitAdapter.adapt(arrow as Entity)
            bullet?.velocity = projectile.currentVelocity
            Projectile.BULLET_ENTITIES.add(this.bullet)
            this@ModelBullet.plugin.volatileCodeHandler.entityHandler.setHitBox(this.bullet, 0.0, 0.0)
            if (projectile.hasTerminated()) {
                bullet?.remove()
            }
        }

        override fun tick(origin: AbstractLocation) {
            val o = bullet!!.location.toVector()
            val n = projectile.currentLocation.toVector()
            val velocity = n.subtract(o).normalize().multiply(projectile.velocityMagnitude)
            if (velocity.isFinite) {
                bullet!!.velocity = velocity
            } else {
                bullet!!.velocity = projectile.currentVelocity
            }
        }

        override fun despawn() {
            bullet!!.remove()
            Projectile.BULLET_ENTITIES.remove(this.bullet)
        }
    }

    companion object {

        /** 速度幅值换算到出生朝向位移的经验除数 */
        private const val VELOCITY_DIVISOR = 7.6

        /** 箭矢穿透等级（设高值使其不被实体阻挡） */
        private const val ARROW_PIERCE_LEVEL = 125

        fun register() {
            val constructor: Constructor<ProjectileBulletType>?
            try {
                constructor = ProjectileBulletType::class.java.getDeclaredConstructor(
                    String::class.java,
                    Class::class.java,
                    Array<String>::class.java
                )
                constructor.isAccessible = true
                constructor.newInstance("MODEL", ModelBullet::class.java, arrayOfNulls<String>(0))
            } catch (e: Exception) {
                bukkitPlugin.sendMessage(L(AXLanguageKey.REGISTER_MODEL_BULLET_FAILED))
            }
        }
    }
}