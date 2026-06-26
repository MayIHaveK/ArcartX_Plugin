/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.editor.data

import org.bukkit.Location
import priv.seventeen.artist.arcartx.nms.AsteroidScheduler

class EditorData {

    var areaSelLocationFirst: Location? = null

    var areaSelLocationSecond: Location? = null

    var areaEditEffectTask: Any? = null

    var sceneCameraLocations: MutableList<Location>? = null;

    var sceneCameraEffectTask: Any? = null

    fun clear() {
        AsteroidScheduler.cancelTask(areaEditEffectTask)
        AsteroidScheduler.cancelTask(sceneCameraEffectTask)
    }

}
