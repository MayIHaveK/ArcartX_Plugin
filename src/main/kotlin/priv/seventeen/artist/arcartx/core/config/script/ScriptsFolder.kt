/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.script

import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigFolder
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.arcartx.script.ScriptManager
import priv.seventeen.artist.blink.bukkitPlugin

class ScriptsFolder : ArcartXConfigFolder<Scripts>(bukkitPlugin, "scripts", { _, path -> Scripts(path) }) {

    init {
        load()
    }

    override fun onCreateFolder(plugin: JavaPlugin, folderPath: String) {
        this.createConfig(plugin,   "${folderPath}脚本示例.yml")
    }

    override fun reload() {
        super.reload()
        this.configs.values.forEach {
            it.scripts.forEach{ (name, element) ->
                if(element.type.equals("normal", true)){
                    ScriptManager.register(name, element.script)
                }
                if(element.type.equals("event", true)){
                    ScriptManager.registerListener(element.target, element.script)
                }
            }
        }

        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_FUNCTION_SCRIPT, ScriptManager.functions.size.toString()))
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_EVENT_SCRIPT, ScriptManager.listeners.size.toString()))
    }
}