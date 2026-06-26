/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.sound

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.network.NetworkMessageSender

/**
 * 音效播放器。
 *
 * 下列方法为 `World` 的 Kotlin 扩展函数，且均为 `@JvmStatic`：
 * - Kotlin：`world.playSoundForPlayer(player, location, ...)`
 * - Java：作为静态方法调用，接收者 world 作为第一个参数——
 *   `ArcartXSoundPlayer.playSoundForPlayer(world, player, location, ...)`
 */
object ArcartXSoundPlayer {

    @JvmStatic
    fun World.playSoundForPlayer(player: Player, location: Location, resourcePath: String, soundCategory: String, distOrRoll: Int, pitch: Double, keepTime: Int) {
        if(player.world == this && player.location.distance(location) < distOrRoll)
            NetworkMessageSender.sendPlaySound(player, resourcePath, location.blockX, location.blockY, location.blockZ, soundCategory, distOrRoll, pitch, keepTime)
    }

    @JvmStatic
    fun World.playSoundOnWorld(location: Location, resourcePath: String, soundCategory: String, distOrRoll: Int, pitch: Double, keepTime: Int) {
        getNearbyEntities(location, distOrRoll.toDouble(), distOrRoll.toDouble(), distOrRoll.toDouble()).parallelStream().forEach { entity ->
            if(entity is Player) {
                NetworkMessageSender.sendPlaySound(entity, resourcePath, location.blockX, location.blockY, location.blockZ, soundCategory, distOrRoll, pitch, keepTime)
            }
        }
    }

}