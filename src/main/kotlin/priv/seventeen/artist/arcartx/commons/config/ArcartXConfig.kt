/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.config

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files

/** YAML 配置文件基类 */
abstract class ArcartXConfig(@field:Transient var plugin: JavaPlugin, pathName: String) : ArcartXConfigSection(),
    ReloadableConfig {
    @Transient
    var configFile: File? = null

    @Transient
    var pathName: String


    init {
        this.pathName = ensureYamlExtension(pathName)
    }

    fun load(configuration: YamlConfiguration?) {
        this.load(this.plugin, this.pathName, configuration)
    }


    fun load() {
        this.configFile = File(plugin.dataFolder, pathName)
        createParentDirsIfNotExist(configFile!!)

        if (!configFile!!.exists()) {
            val inputStream = plugin.getResource("assets/$pathName")
            if (inputStream != null) {
                saveResource(inputStream)
            } else {
                writeResource()
            }
        }
        reload()
    }

    override fun reload() {
        val configuration = YamlConfiguration.loadConfiguration(
            configFile!!
        )
        this.load(this.plugin, this.pathName, configuration)
    }

    private fun writeResource() {
        val builder = StringBuilder()
        this.write(this.plugin, this.pathName, builder)
        writeToFile(builder.toString())
    }

    private fun writeToFile(text: String) {
        try {
            Files.newBufferedWriter(configFile!!.toPath(), StandardCharsets.UTF_8).use { writer ->
                writer.write(text)
            }
        } catch (e: IOException) {
            plugin.sendException(L(AXLanguageKey.EXCEPTION_CONFIG_WRITE, this.pathName), e)
        }
    }

    private fun saveResource(inputStream: InputStream) {
        try {
            inputStream.use { `in` ->
                Files.newOutputStream(configFile!!.toPath()).use { out ->
                    val buf = ByteArray(1024)
                    var len: Int
                    while ((`in`.read(buf).also { len = it }) > 0) {
                        out.write(buf, 0, len)
                    }
                }
            }
        } catch (e: IOException) {
            plugin.sendException(L(AXLanguageKey.EXCEPTION_CONFIG_READ, this.pathName), e)
        }
    }



    private fun ensureYamlExtension(pathName: String): String {
        return (if (pathName.endsWith(".yml")) pathName else "$pathName.yml").replace('\\','/')
    }

    private fun createParentDirsIfNotExist(file: File) {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
    }
}