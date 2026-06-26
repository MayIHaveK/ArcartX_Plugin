/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.keybind

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.config.key.client.ClientKeyElement
import priv.seventeen.artist.arcartx.core.config.key.simple.SimpleKeyElement
import priv.seventeen.artist.arcartx.core.editor.ArcartXEditorManager
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.arcartx.network.NetworkMessageSender
import priv.seventeen.artist.arcartx.util.collections.KeyCallBack
import priv.seventeen.artist.arcartx.util.collections.PlayerCallBack
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.lifecycle.Awake
import priv.seventeen.artist.blink.lifecycle.LifeCycle
import java.util.Collections

/** 按键绑定注册表 */
object ArcartXKeyBindRegistry {

    private val _clientKeyBind: LinkedHashMap<String, ClientKeyElement> = LinkedHashMap()
    private val _simpleKeyBind: HashMap<String, SimpleKeyElement> = HashMap()

    val clientKeyBind: Map<String, ClientKeyElement> = Collections.unmodifiableMap(_clientKeyBind)
    val simpleKeyBind: Map<String, SimpleKeyElement> = Collections.unmodifiableMap(_simpleKeyBind)

    @Awake(LifeCycle.ACTIVE)
    fun init (){
        // 辅助按键注册
        registerSimpleKeyBind("arcartx:alt", mutableListOf("LEFT_ALT"),
            onPress = { player ->
                ArcartXEntityManager.getPlayer(player)?.alt = true },
            onRelease = { player ->
                ArcartXEntityManager.getPlayer(player)?.alt = false
                ArcartXEditorManager.editorDataCache[player.uniqueId]?.sceneCameraLocations?.add(player.location.apply { y+= 1.74 })
            }
        )

        registerSimpleKeyBind("arcartx_reload", mutableListOf("LEFT_ALT","R"),
            onPress = { player ->
                if(player.isOp) {
                    bukkitPlugin.sendMessage(player, L(AXLanguageKey.RELOADING))
                    ArcartX.configs.reload(true)
                    bukkitPlugin.sendMessage(L(AXLanguageKey.RELOAD_COMPLETE))
                }
        })

        registerSimpleKeyBind("arcartx_editor", mutableListOf("LEFT_ALT","I"),
            onPress = { player ->
                if(player.isOp) {
                   NetworkMessageSender.sendOpenEditor(player)
                }
            })

    }

    fun registerSimpleKeyBind(keyName:String, keys: MutableList<String>) {
        val simpleKeyElement = SimpleKeyElement(keys, mutableListOf())
        ArcartX.configs.simpleKeyFolder.element[keyName] = simpleKeyElement
        _simpleKeyBind[keyName] = simpleKeyElement
    }

    // for java
    fun registerSimpleKeyBind(keyName: String, keys: MutableList<String>, callBack: KeyCallBack) {
        val simpleKeyElement = SimpleKeyElement(keys, mutableListOf())
        simpleKeyElement.callBack = callBack
        ArcartX.configs.simpleKeyFolder.element[keyName] = simpleKeyElement
        _simpleKeyBind[keyName] = simpleKeyElement
    }

    fun registerSimpleKeyBind(keyName: String, keys: MutableList<String>, onPress :PlayerCallBack = PlayerCallBack { }, onRelease : PlayerCallBack = PlayerCallBack { }
    ) {
        val simpleKeyElement = SimpleKeyElement(keys, mutableListOf())
        simpleKeyElement.callBack = object : KeyCallBack {
            override fun onPress(player: Player) {
                onPress.call(player)
            }

            override fun onRelease(player: Player) {
                onRelease.call(player)
            }
        }
        ArcartX.configs.simpleKeyFolder.element[keyName] = simpleKeyElement
        _simpleKeyBind[keyName] = simpleKeyElement
    }

    fun registerClientKeyBind(keyName: String, category: String, defaultKey: String) {
        val clientKeyElement = ClientKeyElement(keyName, category, defaultKey)
        ArcartX.configs.clientKeyFolder.elementMap[keyName] = clientKeyElement
        _clientKeyBind[keyName] = clientKeyElement
    }

    fun registerClientKeyBind(keyName: String, category: String, defaultKey: String, callBack: KeyCallBack) {
        val clientKeyElement = ClientKeyElement(keyName, category, defaultKey)
        clientKeyElement.callBack = callBack
        ArcartX.configs.clientKeyFolder.elementMap[keyName] = clientKeyElement
        _clientKeyBind[keyName] = clientKeyElement
    }

    fun registerClientKeyBind(keyName: String, category: String, defaultKey: String, onPress : PlayerCallBack = PlayerCallBack { }, onRelease : PlayerCallBack = PlayerCallBack { }) {
        val clientKeyElement = ClientKeyElement(keyName, category, defaultKey)
        clientKeyElement.callBack = object : KeyCallBack {
            override fun onPress(player: Player) {
                onPress.call(player)
            }

            override fun onRelease(player: Player) {
                onRelease.call(player)
            }
        }
        ArcartX.configs.clientKeyFolder.elementMap[keyName] = clientKeyElement
        _clientKeyBind[keyName] = clientKeyElement
    }

    fun unregisterSimpleKeyBind(keyName: String) {
        ArcartX.configs.simpleKeyFolder.element.remove(keyName)
        _simpleKeyBind.remove(keyName)
    }

    fun unregisterClientKeyBind(keyName: String) {
        ArcartX.configs.clientKeyFolder.elementMap.remove(keyName)
        _clientKeyBind.remove(keyName)
    }

}