/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.script

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.blink.bukkitPlugin
import javax.script.Invocable
import javax.script.ScriptEngine
import priv.seventeen.artist.blink.script.ScriptManager as BlinkScriptManager

/** 脚本引擎管理器 */
object ScriptManager {

    val functions = mutableMapOf<String, String>()

    val listeners = mutableSetOf<Listener>()

    private var index = 0


    private val lock = Any()

    private fun getNextIndex(): String {
        return "script${index++}"
    }

    private var engine: ScriptEngine = BlinkScriptManager.createEngine()

    fun register(name: String, script: String) {
        try {
            synchronized(lock) {
                val id = getNextIndex()
                engine.eval(
                    """
                    function $id(player, itemStack, args) {
                        $script
                    }
                """
                )
                functions[name] = id
            }
        } catch (e: Exception) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_SCRIPT_REGISTER_FAILED, name), e)
        }
    }

    fun registerListener(eventClassName: String, script: String) {
        val eventClass: Class<*>
        try {
            eventClass = Class.forName(eventClassName)
        } catch (e: Exception) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_EVENT_SCRIPT_CLASS_NOT_FOUND, eventClassName), e)
            return
        }
        if (!Event::class.java.isAssignableFrom(eventClass)) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_EVENT_SCRIPT_NOT_EVENT, eventClassName), null)
            return
        }
        try {
            val id = synchronized(lock) {
                val newId = getNextIndex()
                engine.eval(
                    """
                    function $newId(event) {
                        $script
                    }
                """
                )
                newId
            }
            val listener = object : Listener {}
            @Suppress("UNCHECKED_CAST")
            Bukkit.getPluginManager().registerEvent(
                eventClass as Class<out Event>,
                listener,
                EventPriority.NORMAL,
                { _, event -> invokeEventScript(id, event) },
                bukkitPlugin
            )
            listeners.add(listener)
        } catch (e: Exception) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_EVENT_SCRIPT_FAILED, eventClassName), e)
        }
    }

    /** 事件脚本回调，可能在任意线程触发，故在锁内使用当前引擎执行 */
    private fun invokeEventScript(id: String, event: Event) {
        try {
            synchronized(lock) {
                (engine as Invocable).invokeFunction(id, event)
            }
        } catch (e: Exception) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_EVENT_SCRIPT_FAILED, id), e)
        }
    }

    fun reset(imports: Map<String, String>) {
        synchronized(lock) {
            functions.clear()

            engine = BlinkScriptManager.createEngine()

            index = 0

            listeners.forEach {
                HandlerList.unregisterAll(it)
            }
            listeners.clear()

            imports.forEach {
                try {
                    engine.put(it.key, Class.forName(it.value))
                } catch (e: Exception) {
                    bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_SCRIPT_CLASS_NOT_FOUND, it.value), e)
                }
            }
        }
    }

    fun executeScript(name: String, player: Player, itemStack: ItemStack?, args: ScriptArgs): Boolean {
        return try {
            synchronized(lock) {
                val functionName = functions[name] ?: return false
                val result = (engine as Invocable).invokeFunction(functionName, player, itemStack, args)
                result == true || result == java.lang.Boolean.TRUE
            }
        } catch (e: Exception) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_SCRIPT_EXECUTE_FAILED, name), e)
            false
        }
    }

    fun executeScript(name: String, player: Player, args: ScriptArgs): Boolean {
        return executeScript(name, player, null, args)
    }


}
