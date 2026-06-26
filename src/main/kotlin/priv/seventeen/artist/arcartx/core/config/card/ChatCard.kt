/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.card

import com.google.gson.annotations.SerializedName
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.core.config.ui.control.Control
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.annotation.Implicit

class ChatCard (formPlugin: JavaPlugin, pathName: String) : ArcartXConfig(formPlugin, pathName) {

    @SerializedName("id")
    @Implicit
    lateinit var id: String

    @SerializedName("root_control")
    var controls: Control = Control()
        private set

    init {
        this.load()
    }
}