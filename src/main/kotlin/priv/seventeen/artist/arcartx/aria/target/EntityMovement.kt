/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.aria.target

import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import priv.seventeen.artist.aria.annotation.java.AriaInvokeHandler
import priv.seventeen.artist.aria.callable.InvocationData

/** 高级移动语句 - 冲刺、闪避、瞬移等 */
object EntityMovement {

    @JvmStatic
    @AriaInvokeHandler(value = "teleportForward", target = LivingEntity::class)
    fun teleportForward(data: InvocationData) {
        if (data.size() != 1) return
        val distance = data[0].doubleValue()
        val player = data.target as LivingEntity
        val loc = player.location
        val direction = loc.direction.setY(0).normalize()
        var target: Location? = loc.add(direction.multiply(distance))
        target = findSafeLocation(target)
        target?.let { player.teleport(it) }
    }

    @JvmStatic
    @AriaInvokeHandler(value = "teleportBack", target = LivingEntity::class)
    fun teleportBack(data: InvocationData) {
        if (data.size() != 1) return
        val distance = data[0].doubleValue()
        val player = data.target as LivingEntity
        val loc = player.location
        val direction = loc.direction.setY(0).normalize().multiply(-1)
        var target: Location? = loc.add(direction.multiply(distance))
        target = findSafeLocation(target)
        target?.let { player.teleport(it) }
    }

    @JvmStatic
    @AriaInvokeHandler(value = "teleportLeft", target = LivingEntity::class)
    fun teleportLeft(data: InvocationData) {
        if (data.size() != 1) return
        val distance = data[0].doubleValue()
        val player = data.target as LivingEntity
        val loc = player.location
        val direction = loc.direction.setY(0).normalize()
        val left = Vector(direction.z, 0.0, -direction.x)
        var target: Location? = loc.add(left.multiply(distance))
        target = findSafeLocation(target)
        target?.let { player.teleport(it) }
    }

    @JvmStatic
    @AriaInvokeHandler(value = "teleportRight", target = LivingEntity::class)
    fun teleportRight(data: InvocationData) {
        if (data.size() != 1) return
        val distance = data[0].doubleValue()
        val player = data.target as LivingEntity
        val loc = player.location
        val direction = loc.direction.setY(0).normalize()
        val right = Vector(-direction.z, 0.0, direction.x)
        var target: Location? = loc.add(right.multiply(distance))
        target = findSafeLocation(target)
        target?.let { player.teleport(it) }
    }

    @JvmStatic
    @AriaInvokeHandler(value = "teleportUp", target = LivingEntity::class)
    fun teleportUp(data: InvocationData) {
        if (data.size() != 1) return
        val distance = data[0].doubleValue()
        val player = data.target as LivingEntity
        var target: Location? = player.location.add(0.0, distance, 0.0)
        target = findSafeLocation(target)
        target?.let { player.teleport(it) }
    }

    @JvmStatic
    @AriaInvokeHandler(value = "teleportDown", target = LivingEntity::class)
    fun teleportDown(data: InvocationData) {
        if (data.size() != 1) return
        val distance = data[0].doubleValue()
        val player = data.target as LivingEntity
        var target: Location? = player.location.subtract(0.0, distance, 0.0)
        target = findSafeLocation(target)
        target?.let { player.teleport(it) }
    }

    @JvmStatic
    @AriaInvokeHandler(value = "dashAdvanced", target = LivingEntity::class)
    fun dashAdvanced(data: InvocationData) {
        if (data.size() != 3) return
        val forward = data[0].doubleValue()  // 正值=前，负值=后
        val strafe = data[1].doubleValue()   // 正值=右，负值=左
        val vertical = data[2].doubleValue()
        val player = data.target as LivingEntity

        val direction = player.location.direction.setY(0).normalize()
        val right = direction.clone().crossProduct(Vector(0, 1, 0)).normalize()

        val velocity = direction.multiply(forward).add(right.multiply(strafe)).setY(vertical)
        player.velocity = velocity
    }

    private fun dashTowards(data: InvocationData, base: (facing: Vector) -> Vector) {
        if (data.size() != 2) return
        val horizontal = data[0].doubleValue()
        val vertical = data[1].doubleValue()
        val player = data.target as LivingEntity
        val facing = player.location.direction.setY(0).normalize()
        player.velocity = base(facing).multiply(horizontal).setY(vertical)
    }

    @JvmStatic
    @AriaInvokeHandler(value = "dash", target = LivingEntity::class)
    fun dash(data: InvocationData) = dashTowards(data) { it }

    @JvmStatic
    @AriaInvokeHandler(value = "dashBack", target = LivingEntity::class)
    fun dashBack(data: InvocationData) = dashTowards(data) { it.multiply(-1) }

    @JvmStatic
    @AriaInvokeHandler(value = "dashLeft", target = LivingEntity::class)
    fun dashLeft(data: InvocationData) = dashTowards(data) { Vector(it.z, 0.0, -it.x) }

    @JvmStatic
    @AriaInvokeHandler(value = "dashRight", target = LivingEntity::class)
    fun dashRight(data: InvocationData) = dashTowards(data) { Vector(-it.z, 0.0, it.x) }

    @JvmStatic
    @AriaInvokeHandler(value = "jump", target = LivingEntity::class)
    fun jump(data: InvocationData) {
        if (data.size() != 1) return
        val force = data[0].doubleValue()
        val player = data.target as LivingEntity
        val velocity = player.velocity
        velocity.setY(force)
        player.velocity = velocity
    }

    @JvmStatic
    @AriaInvokeHandler(value = "dive", target = LivingEntity::class)
    fun dive(data: InvocationData) {
        if (data.size() != 1) return
        val force = data[0].doubleValue()
        val player = data.target as LivingEntity
        val velocity = player.velocity
        velocity.setY(-force)
        player.velocity = velocity
    }

    @JvmStatic
    @AriaInvokeHandler(value = "bounce", target = LivingEntity::class)
    fun bounce(data: InvocationData) {
        val player = data.target as LivingEntity
        val velocity = player.velocity
        player.velocity = velocity.multiply(-1)
    }

    private fun findSafeLocation(loc: Location?): Location? {
        if (loc == null) return null
        val safe = loc.clone()

        val feet = safe.block
        val head = feet.getRelative(BlockFace.UP)

        if (!feet.type.isSolid && !head.type.isSolid) {
            for (y in 0 until 5) {
                val below = safe.clone().subtract(0.0, (y + 1).toDouble(), 0.0).block
                if (below.type.isSolid) {
                    safe.y = (below.y + 1).toDouble()
                    return safe
                }
            }
            return safe
        }

        for (y in 1..5) {
            val check = safe.clone().add(0.0, y.toDouble(), 0.0)
            val checkFeet = check.block
            val checkHead = checkFeet.getRelative(BlockFace.UP)
            if (!checkFeet.type.isSolid && !checkHead.type.isSolid) {
                return check
            }
        }

        return null
    }
}
