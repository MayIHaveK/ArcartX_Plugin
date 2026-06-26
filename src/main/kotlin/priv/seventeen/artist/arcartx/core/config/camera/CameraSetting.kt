/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.camera

import com.google.gson.annotations.SerializedName
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfig
import priv.seventeen.artist.arcartx.commons.config.annotation.Comment
import priv.seventeen.artist.blink.bukkitPlugin

class CameraSetting : ArcartXConfig(bukkitPlugin,"camera/setting.yml") {

    @Comment("是否开启相机功能")
    @SerializedName("enable")
    var enable = true
        private set

    @Comment("视角锁定模式，0-关闭，1-强制锁定第一人称，2-强制锁定第三人称")
    @SerializedName("forceMode")
    var force = 0
        private set


    @Comment("默认预设，请到preset文件夹创建预设")
    @SerializedName("default")
    var defaultCamera = "idle"
        private set

    init {
        load()
    }

}