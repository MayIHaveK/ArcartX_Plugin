/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.message

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import java.util.logging.Level

/** 插件消息发送器，统一控制台和玩家消息格式 */
class ArcartXSender(plugin: Plugin, prefix: String?){

    companion object {

        private val senders : MutableMap<String, ArcartXSender>  = HashMap()

        @JvmStatic
        private fun getSender(plugin: Plugin): ArcartXSender {
            return senders.getOrPut(plugin.name) { ArcartXSender(plugin, null) }
        }

        @JvmStatic
        fun Plugin.sendMessage(message: String) {
            getSender(this).sendMessage(message)
        }

        @JvmStatic
        fun Plugin.sendDebug(message: String) {
            getSender(this).sendDebug(message)
        }

        @JvmStatic
        fun Plugin.sendException(from: String, exception: Exception?) {
            getSender(this).sendException(from, exception)
        }

        @JvmStatic
        fun Plugin.sendMessage(sender: CommandSender, message: String) {
            getSender(this).sendMessage(sender, message)
        }

        @JvmStatic
        fun Plugin.printStart(type: Int) {
            getSender(this).printStart(type)
        }

        @JvmStatic
        fun Plugin.printStart(name : String, type: Int) {
            ArcartXSender(this,name).printStart(type)
        }

        @JvmStatic
        fun Plugin.setDebugMode(debugMode: Boolean) {
            getSender(this).debugMode = debugMode
        }
    }


    init {
        senders[plugin.name] = this
    }

     private val prefix: String = prefix?:plugin.name

    private val info: String = "&f | &7INFO&f: "

    private val debug: String = "&f | &8DEBUG&7: "

    private val error: String = "&f | &cERROR&f: "

    var debugMode = false

    fun sendMessage(message: String) {
        Bukkit.getServer().consoleSender.sendMessage(
            ("&6♦ $prefix$info$message").replace("&", "§")
        )
    }

    fun sendDebug(message: String) {
        if (!debugMode) return
        Bukkit.getServer().consoleSender.sendMessage(
            ("&6♦ $prefix$debug$message").replace("&", "§")
        )
    }

    fun sendException(from: String, exception: Exception?) {
        Bukkit.getServer().consoleSender.sendMessage(
            ("&6♦ " + prefix + error + "来自 [&b" + from + "&f] ").replace("&", "§"
            )
        )
        // 将 from 上下文带进堆栈日志行，避免与上面的提示脱节、便于日志检索定位
        Bukkit.getLogger().log(Level.WARNING, "[$prefix] 来自 [$from] 的错误", exception)
    }


    fun sendMessage(sender: CommandSender, message: String) {
        sender.sendMessage(
            ("&6♦ $prefix$info$message").replace("&", "§"
            )
        )
    }


    fun printStart(type: Int) {
        if (type == 0) {
            Bukkit.getServer().consoleSender.sendMessage("§9   ___           _ _____ _        _   _     _   ")
            Bukkit.getServer().consoleSender.sendMessage("§9  / __\\_   _    / |___  /_\\  _ __| |_(_)___| |_ ")
            Bukkit.getServer().consoleSender.sendMessage("§9 /__\\// | | |   | |  / //_\\\\| '__| __| / __| __|")
            Bukkit.getServer().consoleSender.sendMessage("§9/ \\/  \\ |_| |_  | | / /  _  \\ |  | |_| \\__ \\ |_ ")
            Bukkit.getServer().consoleSender.sendMessage("§9\\_____/\\__, (_) |_|/_/\\_/ \\_/_|   \\__|_|___/\\__|" + "§9 QQ:652805973")
            Bukkit.getServer().consoleSender.sendMessage("§9       |___/                                    ")
        } else if (type == 1) {
            Bukkit.getServer().consoleSender.sendMessage(" §b    _              §3         _  §1__  __")
            Bukkit.getServer().consoleSender.sendMessage(" §b   / \\   _ __ ___ §3__ _ _ __| |_§1\\ \\/ /")
            Bukkit.getServer().consoleSender.sendMessage(" §b  / _ \\ | '__/ __§3/ _` | '__| __|§1\\  / ")
            Bukkit.getServer().consoleSender.sendMessage("§b  / ___ \\| | | (_§3| (_| | |  | |_ §1/  \\ ")
            Bukkit.getServer().consoleSender.sendMessage("§b /_/   \\_\\_|  \\___§3\\__,_|_|   \\__§1/_/\\_\\   §3By.17Artist §f- §9QQ: 652805973")
            Bukkit.getServer().consoleSender.sendMessage("§f")
        }


        this.sendMessage(L(AXLanguageKey.WELCOME_MESSAGE, this.prefix))
        this.sendMessage(L(AXLanguageKey.PROJECT_URL))
    }


}