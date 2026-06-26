/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.editor.ArcartXEditorManager
import priv.seventeen.artist.arcartx.core.editor.camera.CameraEditor
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.blink.bukkitPlugin

/** 相机控制命令 */
class CameraCommand : MultiCommandExecutor() {
    override val name: String
        get() = "camera"

    private fun Player.nextChat(callback: (String) -> Unit) {
        val listener = object : Listener {
            @EventHandler(priority = EventPriority.LOWEST)
            fun onChat(event: AsyncPlayerChatEvent) {
                if (event.player.uniqueId == this@nextChat.uniqueId) {
                    event.isCancelled = true
                    AsyncPlayerChatEvent.getHandlerList().unregister(this)
                    AsteroidScheduler.runTask(bukkitPlugin, Runnable {
                        callback(event.message)
                    })
                }
            }
        }
        Bukkit.getPluginManager().registerEvents(listener, bukkitPlugin)
    }

    @CommandHandler(
        command = "scene",
        description = "为一个玩家开启一个场景相机",
        permission = "arcartx.command.admin.camera",
        args = ["player", "scene"],
        senderType = SenderType.OP
    )
    fun start(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            NetworkMessageSender.sendSceneCamera(it, context.getArg(1))
        }
    }

    @CommandHandler(
        command = "stop",
        description = "停止一个玩家的场景相机",
        permission = "arcartx.command.admin.camera",
        args = ["player"],
        senderType = SenderType.OP
    )
    fun stop(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            NetworkMessageSender.sendSceneCameraStop(it)
        }
    }

    @CommandHandler(
        command = "createSceneAbs",
        description = "创建一个绝对坐标场景相机",
        permission = "arcartx.command.admin.camera",
        args = [],
        senderType = SenderType.PLAYER
    )
    fun create(context: CommandContext) {
        val player = context.getSender() as Player
        ArcartXEditorManager.getOrCreateEditorData(player).apply {
            sceneCameraLocations = ArrayList()
            sceneCameraEffectTask = AsteroidScheduler.runTaskTimer(bukkitPlugin, Runnable {
                CameraEditor.startDraw(player)
            },0,5)
        }
        ArcartXEntityManager.getPlayer(player)?.let {
            it.setViewLock(false)
            it.setThirdPerson(false)
        }

        context.sendMessage(L(AXLanguageKey.CAMERA_EDIT_ENTER))
        context.sendMessage(L(AXLanguageKey.CAMERA_EDIT_INSTRUCTION))
        context.sendMessage(L(AXLanguageKey.CAMERA_EDIT_EXAMPLE))
        player.nextChat { it ->
            if(it.startsWith("end ") && it.length > 3){
                val name = it.substring(4)
                CameraEditor.createSceneAbsCamera(player,name)
            } else{
                ArcartXEditorManager.getOrCreateEditorData(player).apply {
                    sceneCameraLocations = null
                    AsteroidScheduler.cancelTask(sceneCameraEffectTask)
                    sceneCameraEffectTask = null
                }
                ArcartXEntityManager.getPlayer(player)?.setViewLockMode(ArcartX.configs.cameraSetting.force)
                context.sendMessage(L(AXLanguageKey.CAMERA_EDIT_EXIT))
            }
        }
    }

    @CommandHandler(
        command = "preset",
        description = "为一个玩家设置一个相机预设",
        permission = "arcartx.command.admin.camera",
        args = ["player", "preset"],
        senderType = SenderType.OP
    )
    fun preset(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            NetworkMessageSender.setCameraFromPreset(it, context.getArg(1))
        }
    }



}
