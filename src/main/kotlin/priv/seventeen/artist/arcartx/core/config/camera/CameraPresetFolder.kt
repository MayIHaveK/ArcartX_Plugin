/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.camera

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin

class CameraPresetFolder: ArcartXConfigFolder<CameraPreset>(bukkitPlugin, "camera/preset", ::CameraPreset) {

    val elements: MutableMap<String, CameraElement> = HashMap()

    init {
        load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,  "${folderPath}相机预设示例.yml")
    }

    override fun reload() {
        super.reload()
        this.elements.clear()
        this.configs.values.forEach { this.elements.putAll(it.elements) }
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_CAMERA_PRESET, elements.size.toString()))
    }


}