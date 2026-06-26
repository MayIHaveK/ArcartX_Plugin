/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.config

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/** 配置文件夹加载器 */
abstract class ArcartXConfigFolder<T : ArcartXConfig>(
    private val plugin: JavaPlugin,
    folderName: String,
    private val configFactory: (JavaPlugin, String) -> T
) : ReloadableConfig {

    private val folder: File

    @Volatile
    private var _configs: MutableMap<String, T> = ConcurrentHashMap()

    val configs: MutableMap<String, T> get() = _configs

    val folderName: String

    init {
        this.folderName = ensureFolderName(folderName)
        this.folder = File(plugin.dataFolder, this.folderName)
    }

    protected fun createConfig(plugin: JavaPlugin, filePath: String): T = configFactory(plugin, filePath)

    protected abstract fun onCreateFolder(plugin: JavaPlugin, folderPath: String)

    // 如果文件夹下的文件需要文件ID 可以重写这个方法
    protected open fun onSetFileID(config: T, id: String){}

    fun load() {
        if (!folder.exists() && folder.mkdirs()) {
            this.onCreateFolder(this.plugin, this.folderName)
        }
        this.reload()
    }


    override fun reload() {
        val pathList: MutableSet<String> = HashSet()
        listAllFiles(folder, folder, pathList)

        val newConfigs: MutableMap<String, T> = ConcurrentHashMap()
        for (path in pathList) {
            if (path.endsWith(".yml")) {
                var id = path.substring(0, path.length - 4)
                id = id.replace(File.separator, "/")
                val config = createConfig(this.plugin, folderName + path)
                onSetFileID(config, id)
                newConfigs[id] = config
            }
        }
        _configs = newConfigs
    }

    private fun ensureFolderName(folderName: String): String {
        return if (folderName.endsWith(File.separator)) folderName else folderName + File.separator
    }


    companion object {
        private fun listAllFiles(folder: File, rootFolder: File, pathList: MutableSet<String>) {
            if (!folder.exists()) {
                return
            }
            val files = folder.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory) {
                        listAllFiles(file, rootFolder, pathList)
                    } else {
                        pathList.add(getRelativePath(file, rootFolder))
                    }
                }
            }
        }

        private fun getRelativePath(file: File, rootFolder: File): String {
            val filePath = file.toPath()
            val rootPath = rootFolder.toPath()
            return rootPath.relativize(filePath).toString()
        }
    }
}