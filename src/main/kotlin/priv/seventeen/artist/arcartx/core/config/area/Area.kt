/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.area

import com.google.gson.annotations.SerializedName
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import priv.seventeen.artist.arcartx.core.area.ArcartXAreaManager
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.annotation.Comment
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit
import priv.seventeen.artist.arcartx.script.ScriptArgs
import priv.seventeen.artist.blink.bukkitPlugin

class Area : ArcartXConfig {

    @SerializedName("world")
    var world: String = "none"

    @SerializedName("x1")
    var x1: Int = 0

    @SerializedName("y1")
    var y1: Int = 0

    @SerializedName("z1")
    var z1: Int = 0

    @SerializedName("x2")
    var x2: Int = 0

    @SerializedName("y2")
    var y2: Int = 0

    @SerializedName("z2")
    var z2: Int = 0

    @SerializedName("name")
    @Implicit // 不写出到文件 只为了读取方便
    var name: String = "none"

    @SerializedName("priority")
    var priority: Int = 0

    @SerializedName("enter")
    @Comment("进入的时候执行的脚本 请查看脚本如何使用以了解这里如何填写。无需触发直接删掉该配置项即可")
    var enter: List<String> = ArrayList()

    @SerializedName("leave")
    @Comment("离开的时候执行的脚本 请查看脚本如何使用以了解这里如何填写。无需触发直接删掉该配置项即可")
    var leave: List<String> = ArrayList()

    var enterScript: Set<Pair<String, ScriptArgs>>? = null

    var leaveScript: Set<Pair<String, ScriptArgs>>? = null

    constructor (plugin: JavaPlugin ,path : String) : super( plugin,path) {
        load()
    }

    constructor (path : String,name : String, world: World, position1: Vector, position2: Vector) : super(bukkitPlugin, path) {
        this.world = world.name
        this.name = name
        this.x1 = position1.blockX
        this.y1 = position1.blockY
        this.z1 = position1.blockZ

        this.x2 = position2.blockX
        this.y2 = position2.blockY
        this.z2 = position2.blockZ
        this.priority = 0
        this.load()
    }

    override fun load(plugin: JavaPlugin, filePath: String, configurationSection: ConfigurationSection?) {
        super.load(plugin, filePath, configurationSection)
        enterScript = enter.map { ScriptArgs.parseConfig(it) }.toSet()
        leaveScript = leave.map { ScriptArgs.parseConfig(it) }.toSet()
        ArcartXAreaManager.indexArea(this)
    }

    fun inArea(location: Location): Boolean {
        val worldName = location.world?.name ?: return false
        // 与 ArcartXAreaManager 索引使用同一套世界键归一化，避免副本世界匹配不到
        if (ArcartXAreaManager.worldKey(worldName) != world.lowercase()) return false
        return location.run {
            x.isBetween(x1, x2) && y.isBetween(y1, y2) && z.isBetween(z1, z2)
        }
    }

    fun isNearby(player: Player): Boolean {
        val worldName = player.location.world?.name ?: return false
        if (ArcartXAreaManager.worldKey(worldName) != world.lowercase()) return false
        val center = Location(player.world,(x1 + x2) / 2.0, (y1 + y2) / 2.0, (z1 + z2) / 2.0)
        return player.location.distance(center) < 50
    }


    private fun Double.isBetween(a: Int, b: Int) =
        (this - a) * (this - b) <= 0.0


}