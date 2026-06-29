/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.setting

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Comment
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendDebug
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.setDebugMode
import priv.seventeen.artist.arcartx.script.ScriptManager
import priv.seventeen.artist.blink.bukkitPlugin

@Comment("基本配置")
class Setting(plugin: JavaPlugin = bukkitPlugin, pathName: String = "setting.yml") : ArcartXConfig(plugin, pathName) {



    @Comment("是否开启调试模式")
    @SerializedName("debug")
    var debug = false
        private set

    @Comment("许可证 ID（在 https://arcartx.com 获取，0 表示未填写）")
    @SerializedName("licenseId")
    var licenseId = 0
        private set

    @Comment("许可证密钥")
    @SerializedName("licenseKey")
    var licenseKey = ""
        private set

    @Comment("数据库配置")
    @SerializedName("database")
    var database = Database()
        private set

    @Comment("资源配置")
    @SerializedName("encryptedResourceFiles")
    var resource = mutableMapOf("Example" to Resource())
        private set

    @Comment("资源同步配置")
    @SerializedName("resourceSync")
    var resourceSync = ResourceSync()
        private set

    @Comment("客户端标题")
    @SerializedName("clientTitle")
    var title = "欢迎使用ArcartX,该标题可在setting.yml修改"
        private set

    @Comment("占位符API黑名单,可只写开头,当客户端请求这些占位符时会被拦截")
    @SerializedName("placeholderBlacklist")
    var papiBlacklist: List<String> = mutableListOf("Example_")
        private set

    @Comment("脚本导入")
    @Comment("按需导入，如果你不知道你在做什么，请不要修改这里的内容")
    @Comment("key: 导入名 value: 类路径")
    @SerializedName("script_imports")
    var script = mutableMapOf(
        "Bukkit" to "org.bukkit.Bukkit",
        "ArcartXAPI" to "priv.seventeen.artist.arcartx.api.ArcartXAPI",
        "PlayerUtils" to "priv.seventeen.artist.arcartx.util.PlayerUtils",
        "ItemStackUtils" to "priv.seventeen.artist.arcartx.util.ItemStackUtils",
    )
        private set

    @Comment("客户端拓展检测设置")
    @SerializedName("crc64")
    var crc64Setting = Crc64Setting()
        private set


    @Comment("实体管理器缓存清理间隔")
    @SerializedName("entityCacheClearInterval")
    var entityCacheClearInterval = 6000
        private set

    @Comment("是否启用玩家默认模型(内置 Steve 宿主模型)")
    @Comment("开启后:没有显式模型的玩家入服自动渲染内置默认模型;关闭则不给玩家装默认模型,回退原版渲染")
    @SerializedName("playerDefaultModelEnabled")
    var playerDefaultModelEnabled = true
        private set




    init{
        load()
    }

    override fun reload() {
        super.reload()
        bukkitPlugin.setDebugMode(debug)
        bukkitPlugin.sendDebug("The debugging mode has been enabled")
        bukkitPlugin.sendMessage(L(AXLanguageKey.CLIENT_TITLE, title))
        bukkitPlugin.sendMessage(L(AXLanguageKey.DATABASE_LOADED))
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_ENCRYPTED_RESOURCE, resource.size.toString()))
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_PAPI_BLACKLIST, papiBlacklist.size.toString()))
        ScriptManager.reset(script)
        bukkitPlugin.sendMessage(L(AXLanguageKey.LOAD_SCRIPT_IMPORTS, script.size.toString()))
    }

}

class Database : ArcartXConfigSection(){

    @Comment("该项关闭则为SQLITE存储")
    @SerializedName("enable")
    var enable = false
        private set

    @SerializedName("hostName")
    var hostname = "127.0.0.1"
        private set

    @SerializedName("port")
    var port = "3306"
        private set

    @SerializedName("database")
    var database = "database"
        private set

    @SerializedName("username")
    var username = "root"
        private set

    @SerializedName("password")
    var password = "root"
        private set

    @SerializedName("maxConnections")
    var maxConnections = Runtime.getRuntime().availableProcessors() * 2
        private set

    @SerializedName("connectionTimeout")
    var connection_timeout = 10000
        private set

    @SerializedName("validationTimeout")
    var validation_timeout = 3000
        private set

    @SerializedName("idleTimeout")
    var idle_timeout = 60000
        private set

    @SerializedName("loginTimeout")
    var login_timeout = 5
        private set

    @SerializedName("maxLifetime")
    var max_lifetime = 180000
        private set

    @SerializedName("maximumPoolSize")
    var maximum_pool_size = 10
        private set

    @SerializedName("minimumIdle")
    var minimum_idle = 5
        private set

    @SerializedName("readOnly")
    var read_only = false
        private set

    @SerializedName("properties")
    var properties: MutableList<String> = ArrayList()
        private set

    init {
        properties.add("useSSL=false")
        properties.add("useUnicode=true")
        properties.add("characterEncoding=utf-8")
        properties.add("serverTimezone=UTC")
    }

    fun getUrl(): String {
        val str = StringBuilder()
        for (s in this.properties) {
            str.append("&").append(s)
        }

        return "jdbc:mysql://$hostname:$port/$database?autoReconnect=true$str"
    }
}

class Resource : ArcartXConfigSection(){

    @Comment("填写位于“ArcartX/resource/”目录下的加密压缩文件(.zip)的名称")
    @SerializedName("fileName")
    var name: String = "Example.zip"
        private set

    @Comment("解压密码")
    @SerializedName("password")
    var password: String = "123456789"
        private set
}

class Crc64Setting : ArcartXConfigSection(){

    @SerializedName("enable")
    @Comment("是否启用客户端拓展crc64检测")
     var enable = false
        private set

    @SerializedName("allowPartial")
    @Comment("是否是全匹配模式")
    @Comment("如果是 则需要完全匹配 如果不是 则允许缺少一部分")
    var allowPartial = false
        private set

    @SerializedName("list")
    var list: MutableList<Long> = ArrayList()
        private set



}

class ResourceSync: ArcartXConfigSection(){

    @SerializedName("enable")
    @Comment("是否启用资源资源同步[注意，使用该功能请一定看文档]")
    var enable = false
        private set

    @SerializedName("url")
    @Comment("资源更新API后端地址")
    var url = ""
        private set

    // API密钥
    @SerializedName("apiKey")
    @Comment("API密钥")
    var apiKey = ""
        private set

}