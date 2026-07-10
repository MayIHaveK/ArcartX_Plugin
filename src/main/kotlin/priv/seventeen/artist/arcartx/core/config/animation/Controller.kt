/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.animation

import com.google.gson.annotations.SerializedName
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.blink.bukkitPlugin

/**
 * 客户端动作控制器配置模型（本文件是该格式的唯一权威定义；主插件只负责格式/注册/存储）
 */
class Controller : ArcartXConfig {


    @SerializedName("root")
    var controllers: MutableMap<String, ControllerElement> = LinkedHashMap()
        private set

    constructor(formPlugin: JavaPlugin, pathName: String) : super(formPlugin, pathName) {
        this.load()
    }

    constructor(src: YamlConfiguration) : super( bukkitPlugin, "unknown") {
        this.load(src)
    }
}

class ControllerElement : ArcartXConfigSection() {

    @SerializedName("initial_state")
    var initialState = ""
        private set

    @SerializedName("states")
    var states = LinkedHashMap<String, State>()
        private set
}

class State : ArcartXConfigSection() {

    @SerializedName("animation")
    val animation: String = ""

    @SerializedName("in")
    val inAnimation: Map<String, String> = mutableMapOf()

    @SerializedName("to")
    val outAnimation: Map<String, String> = mutableMapOf()

    @SerializedName("weight")
    val weight = 1.0

    @SerializedName("speed")
    val speed = 1.0

    @SerializedName("inTick")
    val transitionTick = 5

    @SerializedName("outTick")
    val transitionOutTick = -1

    @SerializedName("exclusive")
    val exclusive = false

    @SerializedName("moveLimit")
    val moveLimit = 1.0

    @SerializedName("clientLock")
    val clientLockData: ClientLock = ClientLock()

    @SerializedName("onStart")
    val onStart: String = ""

    @SerializedName("onEnd")
    val onEnd: String = ""

}

class ClientLock : ArcartXConfigSection() {

    @SerializedName("input")
    private val input: List<String> = ArrayList()

    @SerializedName("action")
    private val action: List<String> = ArrayList()



}