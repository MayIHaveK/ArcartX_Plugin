/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.editor.camera

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.config.camera.SceneCamera
import priv.seventeen.artist.arcartx.core.config.camera.SceneElement
import priv.seventeen.artist.arcartx.core.editor.ArcartXEditorManager
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.arcartx.util.ParticleUtils.drawLine
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler
import priv.seventeen.artist.blink.bukkitPlugin

object CameraEditor {

    fun createSceneAbsCamera(player: Player, name : String) {
        val axp = ArcartXEditorManager.getOrCreateEditorData(player)
        if(axp.sceneCameraLocations!!.isEmpty()) {
            bukkitPlugin.sendMessage(player, L(AXLanguageKey.NO_COORDINATES_RECORDED))
        }
        SceneCamera(name).apply {
            for(i in axp.sceneCameraLocations!!.indices){
                elements["step$i"] = SceneElement(axp.sceneCameraLocations!![i])
            }
            this.mode = 1
            load()
            ArcartX.configs.sceneCameraFolder.configs[name] = this
        }
        bukkitPlugin.sendMessage(player, L(AXLanguageKey.CAMERA_CREATE_SUCCESS))
        axp.sceneCameraLocations = null
        AsteroidScheduler.cancelTask(axp.sceneCameraEffectTask)
        axp.sceneCameraEffectTask = null
        ArcartXEntityManager.getPlayer(player)?.setViewLockMode(ArcartX.configs.cameraSetting.force)
    }

    fun startDraw(player: Player){
        val editorData = ArcartXEditorManager.getOrCreateEditorData(player)
        if(editorData.sceneCameraLocations != null){
            drawPath(player,editorData.sceneCameraLocations!!)
        }
    }

    private fun drawPath(player: Player, locations: List<Location>, color: Color = Color.RED, density: Double = 2.0) {
        if (locations.isEmpty()) return
        val dustOptions = Particle.DustOptions(color, 1.0f)
        val directionLength = 3.0
        val step = 1.0 / density
        for (i in 0 until locations.size - 1) {
            val current = locations[i]
            val next = locations[i + 1]
            player.drawLine(
                current.x, current.y, current.z,
                next.x, next.y, next.z,
                step , dustOptions)
        }

        for (loc in locations) {
            val direction = loc.direction.multiply(directionLength)

            val directionDustOptions = Particle.DustOptions(Color.AQUA, 1.0f)
            player.drawLine(
                loc.x, loc.y, loc.z,
                loc.x + direction.x, loc.y + direction.y, loc.z + direction.z,
                step , directionDustOptions)
        }
    }

}