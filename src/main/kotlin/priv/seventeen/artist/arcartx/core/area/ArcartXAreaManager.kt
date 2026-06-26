/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.area

import org.bukkit.Location
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.config.area.Area
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/** 区域管理器 */
object ArcartXAreaManager {

    // Chunk 索引: WorldName -> ChunkKey -> 该Chunk内的区域列表
    private val chunkIndex = ConcurrentHashMap<String, MutableMap<Long, MutableList<Area>>>()

    private fun chunkKey(chunkX: Int, chunkZ: Int): Long =
        (chunkX.toLong() shl 32) or (chunkZ.toLong() and 0xFFFFFFFFL)

    fun worldKey(worldName: String): String {
        if (worldName.startsWith("dungeon_")) {
            val end = worldName.lastIndexOf("_")
            if (end > 8) return worldName.substring(8, end).lowercase()
        }
        return worldName.lowercase()
    }

    fun indexArea(area: Area) {
        val wKey = worldKey(area.world)
        val worldIndex = chunkIndex.getOrPut(wKey) { ConcurrentHashMap() }

        val minChunkX = minOf(area.x1, area.x2) shr 4
        val maxChunkX = maxOf(area.x1, area.x2) shr 4
        val minChunkZ = minOf(area.z1, area.z2) shr 4
        val maxChunkZ = maxOf(area.z1, area.z2) shr 4

        for (cx in minChunkX..maxChunkX) {
            for (cz in minChunkZ..maxChunkZ) {
                val key = chunkKey(cx, cz)
                worldIndex.getOrPut(key) { mutableListOf() }.add(area)
            }
        }
    }

    private fun unIndexArea(area: Area) {
        val wKey = worldKey(area.world)
        val worldIndex = chunkIndex[wKey] ?: return

        val minChunkX = minOf(area.x1, area.x2) shr 4
        val maxChunkX = maxOf(area.x1, area.x2) shr 4
        val minChunkZ = minOf(area.z1, area.z2) shr 4
        val maxChunkZ = maxOf(area.z1, area.z2) shr 4

        for (cx in minChunkX..maxChunkX) {
            for (cz in minChunkZ..maxChunkZ) {
                val key = chunkKey(cx, cz)
                worldIndex[key]?.remove(area)
            }
        }
    }

    fun getArea(location: Location): Area? {
        val worldName = location.world?.name ?: return null
        val wKey = worldKey(worldName)
        val worldIndex = chunkIndex[wKey] ?: return null

        val key = chunkKey(location.blockX shr 4, location.blockZ shr 4)
        val candidates = worldIndex[key] ?: return null

        var priority = Int.MIN_VALUE
        var result: Area? = null
        for (area in candidates) {
            if (area.inArea(location) && area.priority > priority) {
                priority = area.priority
                result = area
            }
        }
        return result
    }

    fun getArea(name: String): Area? {
        return ArcartX.configs.areaFolder.configs[name]
    }

    fun addArea(name: String, loc1: Location, loc2: Location) {
        requireNotNull(loc1.world)
        requireNotNull(loc2.world)
        if (loc1.world != loc2.world) {
            throw IllegalArgumentException("两个位置不在同一个世界")
        }
        if (ArcartX.configs.areaFolder.configs.containsKey(name)) {
            throw IllegalArgumentException("已存在名为&b$name&f的区域")
        }
        val area = Area(ArcartX.configs.areaFolder.folderName + File.separator + name + ".yml", name, loc1.world!!, loc1.toVector(), loc2.toVector())
        ArcartX.configs.areaFolder.configs[name] = area
    }

    fun addArea(player: Player, name: String, loc1: Location, loc2: Location) {
        requireNotNull(loc1.world)
        requireNotNull(loc2.world)
        if (loc1.world != loc2.world) {
            bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_DIFFERENT_WORLD))
            return
        }
        if (ArcartX.configs.areaFolder.configs.containsKey(name)) {
            bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_ALREADY_EXISTS, name))
            return
        }
        val area = Area(ArcartX.configs.areaFolder.folderName + File.separator + name + ".yml", name, loc1.world!!, loc1.toVector(), loc2.toVector())
        ArcartX.configs.areaFolder.configs[name] = area
        bukkitPlugin.sendMessage(player, L(AXLanguageKey.AREA_ADD_SUCCESS, name))
    }

    fun removeArea(name: String) {
        val area = ArcartX.configs.areaFolder.configs.remove(name)
        if (area != null) {
            unIndexArea(area)
            area.configFile?.delete()
        }
    }









}