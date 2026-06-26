/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.core.editor

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.editor.data.EditorData
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/** 编辑器管理器 */
object ArcartXEditorManager {


    val editorDataCache : MutableMap<UUID, EditorData> = ConcurrentHashMap()

    fun getOrCreateEditorData(player: Player): EditorData {
        return editorDataCache.computeIfAbsent(player.uniqueId) { EditorData() }
    }

    fun removeEditorData(uuid: UUID){
        editorDataCache.remove(uuid)?.clear()
    }

}