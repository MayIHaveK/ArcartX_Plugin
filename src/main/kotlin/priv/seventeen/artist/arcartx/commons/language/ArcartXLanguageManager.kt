/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.language

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import java.io.File
import java.io.IOException

/** 多语言管理器 */
class ArcartXLanguageManager<T : LanguageKey>(
    private val plugin: JavaPlugin,
    private val languageClass: Class<T>,
    folder: String? = null
) {

    private val file: File

    init {
        val parentDir = if (folder != null) {
            File(plugin.dataFolder, folder).also { it.mkdirs() }
        } else {
            plugin.dataFolder
        }
        this.file = File(parentDir, "language.yml")
        this.load()
    }

    fun getMessage(language: T, vararg args: Any): String {
        var message: String = language.getEnumMessage()
        message = message.replace("&","§")
        var i = 0
        while (i < args.size) {
            message = message.replace("{$i}", args[i].toString())
            i ++
        }
        return message
    }



    fun reload() {
        this.load()
    }

    private fun load() {
        if (!file.exists()) {
            this.write()
        } else {
            val configuration = YamlConfiguration.loadConfiguration(this.file)
            if (languageClass.isEnum) {
                for (language in languageClass.enumConstants) {
                    language.setEnumMessage(configuration.getString(language.getEnumName(), language.getEnumMessage())!!)
                }
            }
        }
    }

    private fun write() {
        val configuration = YamlConfiguration()
        if (languageClass.isEnum) {
            for (language in languageClass.enumConstants) {
                configuration[language.getEnumName()] = language.getEnumMessage()
            }
        }
        try {
            configuration.save(file)
        } catch (e: IOException) {
            plugin.sendException("Language file write", e)
        }
    }
}
