/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.ui.type

import com.google.gson.annotations.SerializedName
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.core.config.ui.control.Control
import priv.seventeen.artist.arcartx.core.config.ui.task.UITask
import priv.seventeen.artist.arcartx.core.ui.adapter.ArcartXUI
import priv.seventeen.artist.arcartx.core.ui.adapter.CallBackType
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit
import priv.seventeen.artist.arcartx.util.collections.UICallBack
import priv.seventeen.artist.blink.bukkitPlugin
import java.util.*

class UI : ArcartXConfig , ArcartXUI {

    @Transient
    override val callbacks: MutableMap<CallBackType, MutableList<UICallBack>> = EnumMap(CallBackType::class.java)

    @SerializedName("ui")
    private val menuData = UISetting()



    @SerializedName("controls")
    private val controls = LinkedHashMap<String, Control>()

    @SerializedName("template")
    private val template = LinkedHashMap<String, Control>()

    @SerializedName("tasks")
    private val task = LinkedHashMap<String, UITask>()

    @SerializedName("id")
    @Implicit
    override lateinit var id: String

    constructor(formPlugin: JavaPlugin, pathName: String) : super(formPlugin, pathName) {
        this.load()
    }

    constructor(id: String, src: YamlConfiguration) : super( bukkitPlugin, "unknown") {
        this.load(src)
        this.id = id
    }
}


class UISetting : ArcartXConfigSection() {

    @SerializedName("match")
    var math: List<String> = ArrayList()

    @SerializedName("hide")
    var hide: List<String> = ArrayList()


    @SerializedName("itemSize")
    var itemSize = "16"

    @SerializedName("through")
    var through = "false"

    @SerializedName("escClose")
    var escClose = "true"

    @SerializedName("background")
    var background = "true"

    @SerializedName("closeDied")
    var closeDied = "true"

    @SerializedName("show")
    var show = "true"

    @SerializedName("jei")
    var jei = "false"

    @SerializedName("level")
    var level = "0" // 优先级

    @SerializedName("screenScale")
    var screenScale = true

    @SerializedName("action")
    var actions: MutableMap<String, String> = HashMap()

    @SerializedName("packetHandler")
    var packetHandler: MutableMap<String, String> = HashMap()

    @SerializedName("isHud")
    var isHud = "false"

    @SerializedName("defaultOpen")
    var defaultOpen = "true"

}