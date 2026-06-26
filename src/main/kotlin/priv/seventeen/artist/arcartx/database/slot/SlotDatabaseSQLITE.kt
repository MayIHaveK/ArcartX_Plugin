/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.database.slot

import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

/** SQLite 槽位数据库实现 */
class SlotDatabaseSQLITE : SlotDatabase {

    private val dbFile = bukkitPlugin.dataFolder.absolutePath + File.separator + "database.db"
    private val table = "slot"
    private var connection: Connection? = null

    /** SQLite 使用单一共享 Connection（非线程安全），异步访问须串行化 */
    private val lock = Any()

    init {
        initializeDatabase()
    }

    private fun initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection("jdbc:sqlite:$dbFile")
            connection?.createStatement()?.use { stmt ->
                val createTableSQL = "CREATE TABLE IF NOT EXISTS $table (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "player TEXT NOT NULL," +
                        "slotName TEXT NOT NULL," +
                        "item TEXT NOT NULL," +
                        "UNIQUE(player, slotName))"
                stmt.executeUpdate(createTableSQL)
                bukkitPlugin.sendMessage(L(AXLanguageKey.DB_SLOT_CREATED_SQLITE))
            }
        } catch (e: ClassNotFoundException) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_DB_INIT_FAILED), e)
        } catch (e: SQLException) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_DB_INIT_FAILED), e)
        }
    }

    override fun loadPlayerSlotDataRaw(player: UUID, slots: List<String>): Map<String, String> {
        val slotData = HashMap<String, String>()
        val conn = connection ?: return slotData
        if (slots.isEmpty()) return slotData

        val placeholders = slots.joinToString(",") { "?" }
        val sql = "SELECT slotName, item FROM $table WHERE player = ? AND slotName IN ($placeholders)"

        synchronized(lock) {
            try {
                conn.prepareStatement(sql).use { ps ->
                    ps.setString(1, player.toString())
                    for (i in slots.indices) {
                        ps.setString(i + 2, slots[i])
                    }
                    ps.executeQuery().use { rs ->
                        while (rs.next()) {
                            slotData[rs.getString("slotName")] = rs.getString("item")
                        }
                    }
                }
            } catch (e: SQLException) {
                bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_LOAD_SLOT_DATA_FAILED), e)
            }
        }

        return slotData
    }

    override fun setSlotDataJson(player: UUID, slotName: String, itemJson: String) {
        val conn = connection ?: return
        synchronized(lock) {
            try {
                conn.prepareStatement(
                    "INSERT OR REPLACE INTO $table (player, slotName, item) VALUES (?, ?, ?)"
                ).use { ps ->
                    ps.setString(1, player.toString())
                    ps.setString(2, slotName)
                    ps.setString(3, itemJson)
                    ps.executeUpdate()
                }
            } catch (e: SQLException) {
                bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_SET_SLOT_DATA_FAILED), e)
            }
        }
    }

    override fun close() {
        synchronized(lock) {
            try {
                connection?.close()
                connection = null
                bukkitPlugin.sendMessage(L(AXLanguageKey.DB_SLOT_CLOSED_SQLITE))
            } catch (e: SQLException) {
                bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_DB_CLOSE_FAILED), e)
            }
        }
    }
}