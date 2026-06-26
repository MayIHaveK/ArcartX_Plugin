/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.editor.area

import org.bukkit.Bukkit
import org.bukkit.Location
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.area.ArcartXAreaManager
import priv.seventeen.artist.arcartx.core.editor.ArcartXEditorManager
import priv.seventeen.artist.arcartx.core.editor.data.EditorData
import priv.seventeen.artist.arcartx.core.entity.data.ArcartXPlayer
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.arcartx.util.ParticleUtils.drawLine
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.blink.bukkitPlugin
import kotlin.math.max
import kotlin.math.min

object ArcartXAreaEditor {

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

    fun checkTool(item: ItemStack): Boolean =
        !item.type.isAir && ItemTag.fromItemStack(item)["tool"]?.asString()?.equals("area", ignoreCase = true) == true


    fun startDraw(player :Player){
        val editorData = ArcartXEditorManager.getOrCreateEditorData(player)
        if(editorData.areaSelLocationFirst != null && editorData.areaSelLocationSecond != null){
            drawCuboidEdges(player,editorData.areaSelLocationFirst!!, editorData.areaSelLocationSecond!!, Particle.FLAME, 2.0)
        }
        ArcartX.configs.areaFolder.configs.values.forEach{
            if(it.isNearby(player)){
                drawCuboidEdges(player,
                    it.x1 + 0.5 ,it.y1  + 0.5 ,it.z1 + 0.5 ,
                    it.x2 + 0.5 ,it.y2 + 0.5 ,it.z2 + 0.5 ,
                    Particle.VILLAGER_HAPPY,1.0)
            }
        }
    }


    fun handleLeftClick(player: Player,arcartPlayer: ArcartXPlayer, block: Block) {
        when {
            arcartPlayer.alt -> handleClear(player)
            player.isSneaking -> handleAreaPriorityChange(player, block)
            else -> {
                ArcartXEditorManager.getOrCreateEditorData(player).areaSelLocationFirst = block.location
                bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_LEFT_CLICK_SELECTED))
            }
        }
    }

    private fun handleClear(player: Player) {
        ArcartXEditorManager.getOrCreateEditorData(player).areaSelLocationFirst = null
        ArcartXEditorManager.getOrCreateEditorData(player).areaSelLocationSecond = null
        bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_SELECTION_CLEARED))
    }

    fun handleRightClick(player: Player, arcartPlayer: ArcartXPlayer, block: Block) {
        when {
            arcartPlayer.alt -> handleAreaDeletion(player, block)
            player.isSneaking -> handleAreaCreation(player, ArcartXEditorManager.getOrCreateEditorData(player))
            else -> {
                ArcartXEditorManager.getOrCreateEditorData(player).areaSelLocationSecond  = block.location
                bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_RIGHT_CLICK_SELECTED))
            }
        }
    }

    private fun handleAreaPriorityChange(player: Player, block: Block) {
        val target = ArcartXAreaManager.getArea(block.location)
        if (target == null) {
            bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_NOT_FOUND))
            return
        }
        bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_PRIORITY_PROMPT, target.name))
        player.nextChat { input ->
            try {
                val priority = input.toInt()
                target.apply {
                    this.priority = priority
                    this.configFile?.delete()
                    this.load()
                    bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_PRIORITY_SET, priority.toString()))
                }
            } catch (e: Exception) {
                bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_INPUT_NUMBER))
            }
        }
    }

    private fun handleAreaDeletion(player: Player, block: Block) {
        val target = ArcartXAreaManager.getArea(block.location)
        if (target != null) {
            ArcartXAreaManager.removeArea(target.name)
            bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_DELETED, target.name))
        } else {
            bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_NOT_FOUND))
        }
    }

    private fun handleAreaCreation(player: Player, editorData: EditorData) {
        when {
            editorData.areaSelLocationFirst  == null ->
                bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_SELECT_LEFT_FIRST))
            editorData.areaSelLocationSecond == null ->
                bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_SELECT_RIGHT_FIRST))
            else -> {
                bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_INPUT_NAME))
                player.nextChat { name ->
                    if (name.isNotBlank()) {
                        ArcartXAreaManager.addArea(player, name, editorData.areaSelLocationFirst!!, editorData.areaSelLocationSecond!!)
                        editorData.areaSelLocationFirst = null
                        editorData.areaSelLocationSecond = null
                    } else {
                        bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_INPUT_VALID_NAME))
                    }
                }
            }
        }
    }

    private fun drawCuboidEdges(player: Player, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, particleType: Particle, density: Double = 0.5) {
        val minX = min(x1, x2)
        val minY = min(y1, y2)
        val minZ = min(z1,z2)
        val maxX = max(x1,x2)
        val maxY = max(y1,y2)
        val maxZ = max(z1, z2)

        val step = 1.0 / density

        for (i in 0..12) {
            when (i) {
                0 -> player.drawLine( minX, minY, minZ, maxX, minY, minZ, step, particleType)
                1 -> player.drawLine( maxX, minY, minZ, maxX, minY, maxZ, step, particleType)
                2 -> player.drawLine( maxX, minY, maxZ, minX, minY, maxZ, step, particleType)
                3 -> player.drawLine( minX, minY, maxZ, minX, minY, minZ, step, particleType)

                4 -> player.drawLine( minX, maxY, minZ, maxX, maxY, minZ, step, particleType)
                5 -> player.drawLine( maxX, maxY, minZ, maxX, maxY, maxZ, step, particleType)
                6 -> player.drawLine( maxX, maxY, maxZ, minX, maxY, maxZ, step, particleType)
                7 -> player.drawLine( minX, maxY, maxZ, minX, maxY, minZ, step, particleType)

                8 -> player.drawLine( minX, minY, minZ, minX, maxY, minZ, step, particleType)
                9 -> player.drawLine( maxX, minY, minZ, maxX, maxY, minZ, step, particleType)
                10 -> player.drawLine( maxX, minY, maxZ, maxX, maxY, maxZ, step, particleType)
                11 -> player.drawLine( minX, minY, maxZ, minX, maxY, maxZ, step, particleType)
            }
        }
    }


    private fun drawCuboidEdges(player: Player,loc1: Location, loc2: Location, particleType: Particle, density: Double = 0.5) {
        drawCuboidEdges(player, loc1.x + 0.5 , loc1.y + 0.5 , loc1.z + 0.5 , loc2.x + 0.5 , loc2.y + 0.5 , loc2.z + 0.5 , particleType, density)
    }

}