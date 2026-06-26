/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.ui.task

import com.google.gson.annotations.SerializedName
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection

class UITask: ArcartXConfigSection() {

    // 类型
    @SerializedName("type")
    var type = "delay"  // loop |  delay

    @SerializedName("time")
    var time = 0L

    @SerializedName("cycle")
    var cycle = 0L

    @SerializedName("run")
    var run = ""

}