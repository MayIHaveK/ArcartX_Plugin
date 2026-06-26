/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.server

import com.google.gson.annotations.SerializedName
import priv.seventeen.artist.arcartx.ArcartX

class SPackResourceReload(
    @SerializedName("reload")
    val reload: Boolean
) : ServerPacket {

    @SerializedName("initCode")
    private val passwords: MutableMap<String, String> = HashMap()

    @SerializedName("files")
    private var files: Map<String, String> = HashMap()


    init {
        ArcartX.configs.setting.resource.values.forEach { v ->
            run {
                passwords[v.name] = v.password
            }
        }
    }

    constructor(files: Map<String, String>) : this(false){
        this.files = files
    }

}