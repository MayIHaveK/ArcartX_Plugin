/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.nms

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import priv.seventeen.artist.asteroid.AsteroidAPI
import priv.seventeen.artist.asteroid.entity.CustomEntity
import priv.seventeen.artist.asteroid.entity.CustomEntityFactory
import priv.seventeen.artist.asteroid.entity.ability.*

/** 实体 NMS 桥接，封装 Asteroid 实体操作与完整 Ability 系统 */
object EntityBridge {

    private val entityNMS by lazy { AsteroidAPI.getEntityNMS() }
    private val factory by lazy { AsteroidAPI.getCustomEntityFactory() }

    // ==================== 基础实体操作 ====================

    fun setEntitySize(entity: Entity, width: Float, height: Float) {
        entityNMS.setSize(entity, width, height)
    }

    fun doWithSeenBy(entity: Entity, action: (Player) -> Unit) {
        entityNMS.doWithSeenBy(entity, action)
    }

    fun setEntityPos(entity: Entity, x: Double, y: Double, z: Double) {
        entityNMS.setPosition(entity, x, y, z)
    }

    fun setEntityRotation(entity: Entity, yaw: Float, pitch: Float) {
        entityNMS.setRotation(entity, yaw, pitch)
    }

    fun isMoveKeyDown(player: Player): Boolean {
        return entityNMS.isMoveKeyDown(player)
    }

    // ==================== CustomEntity 创建 ====================

    fun createCustomEntity(owner: Entity, width: Double, height: Double): CustomEntity {
        return AsteroidAPI.createCustomEntity(owner, width, height)
    }

    fun createCustomEntity(location: Location, width: Double, height: Double): CustomEntity {
        return AsteroidAPI.createCustomEntity(location, width, height)
    }

    fun getCustomEntityFactory(): CustomEntityFactory {
        return factory
    }

    // ==================== Ability 便捷操作 ====================

    fun addHitbox(
        entity: CustomEntity,
        width: Double,
        height: Double,
        offsetX: Double = 0.0,
        offsetY: Double = 0.0,
        offsetZ: Double = 0.0
    ): HitboxAbility {
        val ability = HitboxAbility(width, height)
        if (offsetX != 0.0 || offsetY != 0.0 || offsetZ != 0.0) {
            ability.setOffset(offsetX, offsetY, offsetZ)
        }
        entity.addAbility(ability)
        return ability
    }

    fun addSeat(entity: CustomEntity): SeatAbility {
        var ability = entity.getAbility(SeatAbility::class.java)
        if (ability == null) {
            ability = SeatAbility()
            entity.addAbility(ability)
        }
        return ability
    }

    fun addSeatOffset(entity: CustomEntity, x: Double, y: Double, z: Double) {
        addSeat(entity).addSeat(x, y, z)
    }

    fun clearSeats(entity: CustomEntity) {
        entity.getAbility(SeatAbility::class.java)?.let {
            entity.removeAbility(SeatAbility::class.java)
        }
        entity.nmsClearSeats()
    }

    fun addDamageHandler(entity: CustomEntity, callback: DamageAbility.Callback): DamageAbility {
        entity.removeAbility(DamageAbility::class.java)
        val ability = DamageAbility(callback)
        entity.addAbility(ability)
        return ability
    }

    fun addFollowOwner(entity: CustomEntity, removeOnOwnerInvalid: Boolean = true): FollowOwnerAbility {
        entity.removeAbility(FollowOwnerAbility::class.java)
        val ability = FollowOwnerAbility(removeOnOwnerInvalid)
        entity.addAbility(ability)
        return ability
    }

    fun addCustomTick(entity: CustomEntity, action: (CustomEntity) -> Unit): CustomTickAbility {
        entity.removeAbility(CustomTickAbility::class.java)
        val ability = CustomTickAbility(action)
        entity.addAbility(ability)
        return ability
    }

    fun addResize(entity: CustomEntity, callback: ResizeAbility.Callback): ResizeAbility {
        entity.removeAbility(ResizeAbility::class.java)
        val ability = ResizeAbility(callback)
        entity.addAbility(ability)
        return ability
    }

    fun addRemove(entity: CustomEntity, callback: RemoveAbility.Callback): RemoveAbility {
        entity.removeAbility(RemoveAbility::class.java)
        val ability = RemoveAbility(callback)
        entity.addAbility(ability)
        return ability
    }

    fun addInteract(entity: CustomEntity, callback: InteractAbility.Callback): InteractAbility {
        entity.removeAbility(InteractAbility::class.java)
        val ability = InteractAbility(callback)
        entity.addAbility(ability)
        return ability
    }

    fun addMount(entity: CustomEntity, type: MountAbility.MountType): MountAbility {
        entity.removeAbility(MountAbility::class.java)
        val ability = MountAbility(type)
        entity.addAbility(ability)
        return ability
    }
}
