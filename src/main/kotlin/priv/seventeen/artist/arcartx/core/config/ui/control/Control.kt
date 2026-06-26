/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.config.ui.control

import com.google.gson.annotations.SerializedName
import priv.seventeen.artist.arcartx.commons.config.ArcartXConfigSection

class Control : ArcartXConfigSection() {

    @SerializedName("type")
    var type = "none"
        private set

    @SerializedName("val")
    var valName = ""
        private set

    @SerializedName("attribute")
    var attributes: Map<String, Any> = HashMap()
        private set

    @SerializedName("effect")
    var effect: Map<String, Any> = HashMap()

    @SerializedName("action")
    var action: Map<String, String> = HashMap()
        private set

    @SerializedName("children")
    var children = LinkedHashMap<String, Control>()
        private set

}
