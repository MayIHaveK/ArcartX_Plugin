/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.effect.data

import com.google.gson.annotations.SerializedName
import org.bukkit.Location
import org.bukkit.entity.Entity
import java.util.*

class EffectPosition private constructor
    (
    @SerializedName("x")
    private val x: Double = 0.0,
    @SerializedName("y")
    private val y: Double = 0.0,
    @SerializedName("z")
    private val z: Double = 0.0,
    @SerializedName("pitch")
    private val pitch: Float = 0f,
    @SerializedName("yaw")
    private val yaw: Float = 0f,
    @SerializedName("roll")
    private val roll: Float = 0f,
    @SerializedName("followEntity")
    private val bindEntity: UUID? = null,
    @SerializedName("followPitch")
    private val followPitch: Boolean = false,
    @SerializedName("followYaw")
    private val followYaw: Boolean = false
    ){

    companion object{

        @JvmStatic
        fun followEntity(entity: Entity): EffectPosition {
            return EffectPosition(0.0, 0.0, 0.0, 0f, 0f,0F,   entity.uniqueId, followPitch = true, followYaw = true)
        }

        @JvmStatic
        fun followEntity(entity: Entity, pitch: Float, yaw: Float,followPitch: Boolean, followYaw: Boolean): EffectPosition {
            return EffectPosition(0.0, 0.0, 0.0, pitch, yaw,0F,  entity.uniqueId, followPitch, followYaw)
        }

        @JvmStatic
        fun followEntity(entity: Entity,
                         xOffset: Double = 0.0, yOffset: Double = 0.0, zOffset: Double = 0.0,
                         pitch: Float = 0.0F, yaw: Float = 0.0F , roll: Float = 0.0F,
                         followPitch: Boolean = true, followYaw: Boolean = true): EffectPosition {
            return EffectPosition( xOffset, yOffset, zOffset, pitch, yaw,roll,  entity.uniqueId, followPitch, followYaw)
        }

        @JvmStatic
        fun followEntity(entity: UUID,
                         xOffset: Double = 0.0, yOffset: Double = 0.0, zOffset: Double = 0.0,
                         pitch: Float = 0.0F, yaw: Float = 0.0F , roll: Float = 0.0F,
                         followPitch: Boolean = true, followYaw: Boolean = true): EffectPosition {
            return EffectPosition( xOffset, yOffset, zOffset, pitch, yaw,roll,   entity, followPitch, followYaw)
        }

        @JvmStatic
        fun location(x: Double, y: Double, z: Double, pitch: Float = 0f, yaw: Float = 0f): EffectPosition {
            return EffectPosition(x, y, z, pitch, yaw )
        }

        @JvmStatic
        fun location(location: Location): EffectPosition {
            return EffectPosition(location.x, location.y, location.z, location.pitch, location.yaw )
        }

        @JvmStatic
        fun location(location: Location, roll: Float = 0F): EffectPosition {
            return EffectPosition(location.x, location.y, location.z, location.pitch, location.yaw, roll )
        }


    }




}