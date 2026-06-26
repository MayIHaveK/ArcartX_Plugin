/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.database.slot

import com.zaxxer.hikari.HikariDataSource
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.core.config.setting.Database
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import java.sql.SQLException
import java.util.*

/** MySQL 槽位数据库实现 */
class SlotDatabaseMYSQL(sqlConfig: Database) : SlotDatabase{

    private val dataSource: HikariDataSource by lazy { HikariDataSource() }

    private val table = "ax_slot"

    init {
        dataSource.apply {
            jdbcUrl = sqlConfig.getUrl()
            username = sqlConfig.username
            password = sqlConfig.password
            addDataSourceProperty("cachePrepStmts", true)
            addDataSourceProperty("prepStmtCacheSize", 250)
            addDataSourceProperty("prepStmtCacheSqlLimit", 2048)
            addDataSourceProperty("useServerPrepStmts", true)
            leakDetectionThreshold = 10000L
            poolName = "$table-Connection-Pool"

            connectionTimeout = sqlConfig.connection_timeout.toLong()
            validationTimeout = sqlConfig.validation_timeout.toLong()
            idleTimeout = sqlConfig.idle_timeout.toLong()
            maxLifetime = sqlConfig.max_lifetime.toLong()
            maximumPoolSize = sqlConfig.maximum_pool_size
            minimumIdle = sqlConfig.minimum_idle
            isReadOnly = sqlConfig.read_only
        }
        try {
            dataSource.loginTimeout = sqlConfig.login_timeout
        } catch (e: SQLException) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_DB_LOGIN_TIMEOUT_FAILED), e)
        }

        try {
            dataSource.connection.use { conn ->
                conn.createStatement().use { stmt ->

                    val createTableSQL = ("CREATE TABLE IF NOT EXISTS " + table + " ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY,"
                            + "player VARCHAR(36) NOT NULL,"
                            + "slotName VARCHAR(255) NOT NULL,"
                            + "item MEDIUMTEXT NOT NULL,"
                            + "UNIQUE KEY player_slot (player, slotName))")

                    stmt.executeUpdate(createTableSQL)
                    bukkitPlugin.sendMessage(L(AXLanguageKey.DB_SLOT_CREATED_MYSQL))
                }
            }
        } catch (e: SQLException) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_CREATE_SLOT_DB_FAILED), e)
        }
    }

    override fun loadPlayerSlotDataRaw(player: UUID, slots: List<String>): Map<String, String> {
        val slotData: MutableMap<String, String> = HashMap()
        if (slots.isEmpty()) {
            return slotData
        }
        val sql = StringBuilder()
        sql.append("SELECT slotName, item FROM ").append(table)
            .append(" WHERE player = ? AND slotName IN (")

        val placeholders = Array(slots.size) { "?" }.joinToString(",")
        sql.append(placeholders).append(")")

        try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql.toString()).use { ps ->
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
            }
        } catch (e: SQLException) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_QUERY_SLOT_DATA_FAILED), e)
        }

        return slotData
    }

    override fun setSlotDataJson(player: UUID, slotName: String, itemJson: String) {
        try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(
                    "INSERT INTO " + table + " (player, slotName, item) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE item = ?"
                ).use { ps ->
                    ps.setString(1, player.toString())
                    ps.setString(2, slotName)
                    ps.setString(3, itemJson)
                    ps.setString(4, itemJson)
                    ps.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            bukkitPlugin.sendException(L(AXLanguageKey.EXCEPTION_SET_SLOT_DATA_FAILED), e)
        }
    }

    override fun close() {
        if (!dataSource.isClosed) {
            dataSource.close()
            bukkitPlugin.sendMessage(L(AXLanguageKey.DB_SLOT_CLOSED_MYSQL))
        }
    }
}