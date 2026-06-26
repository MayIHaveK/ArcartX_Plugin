/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

import org.bukkit.Particle
import org.bukkit.entity.Player
import kotlin.math.pow
import kotlin.math.sqrt

/** 粒子工具类 */
object ParticleUtils {

    fun Player.drawLine(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, step: Double, particleType: Particle) {
        val distance = sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2) + (z2 - z1).pow(2))
        val points = (distance / step).toInt()

        if (points <= 0) return

        for (i in 0..points) {
            val t =  i.toDouble() / points
            val x = x1 + (x2 - x1) * t
            val y = y1 + (y2 - y1) * t
            val z = z1 + (z2 - z1) * t

            this.spawnParticle(particleType, x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
        }
    }

    fun Player.drawLine(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, step: Double, dust : Particle.DustOptions) {
        val distance = sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2) + (z2 - z1).pow(2))
        val points = (distance / step).toInt()

        if (points <= 0) return

        for (i in 0..points) {
            val t =  i.toDouble() / points
            val x = x1 + (x2 - x1) * t
            val y = y1 + (y2 - y1) * t
            val z = z1 + (z2 - z1) * t

            this.spawnParticle(Particle.REDSTONE, x, y, z, 1, 0.0, 0.0, 0.0, 0.0,dust)
        }
    }

}