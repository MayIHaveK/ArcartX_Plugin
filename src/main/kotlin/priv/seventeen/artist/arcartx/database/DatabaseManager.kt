/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.database

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import priv.seventeen.artist.arcartx.core.config.setting.Database
import priv.seventeen.artist.arcartx.database.slot.SlotDatabase
import priv.seventeen.artist.arcartx.database.slot.SlotDatabaseMYSQL
import priv.seventeen.artist.arcartx.database.slot.SlotDatabaseSQLITE

/** 数据库管理器 */
class DatabaseManager(sqlConfig: Database) {

    init {
        Configurator.setLevel("com.zaxxer.hikari", Level.ERROR)
    }

    val slotDatabase : SlotDatabase =
        if(sqlConfig.enable){
            SlotDatabaseMYSQL(sqlConfig)
        } else {
            SlotDatabaseSQLITE()
        }



    fun close() {
        slotDatabase.close()
    }

}