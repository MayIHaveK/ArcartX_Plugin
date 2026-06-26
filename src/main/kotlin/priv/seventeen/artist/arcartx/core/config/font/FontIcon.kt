/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.font

import com.google.gson.annotations.SerializedName
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection
import priv.seventeen.artist.arcartx.commons.config.annotation.Comment

class FontIcon: ArcartXConfigSection() {

    @Comment("图标id的取值范围是0 ~ 999999")
    @SerializedName("id")
    var id = 0
        private set

    @SerializedName("proportion")
    @Comment("和文字大小的比例，默认0.8")
    var proportion = 0.8
        private set

    @SerializedName("path")
    var path = "Example.png"
        private set

}