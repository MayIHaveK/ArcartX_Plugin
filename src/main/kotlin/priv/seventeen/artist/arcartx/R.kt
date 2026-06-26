/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx

import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.profile.PlayerProfile
import priv.seventeen.artist.blink.bukkitPlugin
import java.time.Duration
import java.time.Instant
import java.util.*

val blockNamespace = NamespacedKey(bukkitPlugin, "arcartx_model")

val blockItemNamespace = NamespacedKey(bukkitPlugin, "arcartx_model_from")

/**
 * 用于方块模型 SkullMeta 的虚拟 OfflinePlayer 实例，
 * 仅实现了必要的 getName / getUniqueId 等方法，
 * 其余方法均抛出 UnsupportedOperationException
 */
val owningPlayer = object : OfflinePlayer {

    override fun getName(): String = "_arcartx_model_"

    override fun getUniqueId(): UUID = UUID.fromString("adfdef5f-dad1-38e6-83d9-f971c3568bdd")

    override fun isOp(): Boolean = false
    override fun isOnline(): Boolean = false
    override fun isWhitelisted(): Boolean = false
    override fun isBanned(): Boolean = false

    override fun setOp(value: Boolean) {}
    override fun setWhitelisted(value: Boolean) {}

    override fun getRespawnLocation(): Location? = null
    override fun getLocation(): Location? = null

    private fun unsupported(): Nothing =
        throw UnsupportedOperationException("虚拟 OfflinePlayer 不支持此操作")

    override fun serialize(): MutableMap<String, Any> = unsupported()
    override fun getPlayerProfile(): PlayerProfile = unsupported()
    override fun getPlayer(): Player? = unsupported()
    override fun getFirstPlayed(): Long = unsupported()
    override fun getLastPlayed(): Long = unsupported()
    override fun hasPlayedBefore(): Boolean = unsupported()
    override fun getBedSpawnLocation(): Location? = unsupported()
    override fun getLastDeathLocation(): Location? = unsupported()

    override fun incrementStatistic(p0: Statistic) = unsupported()
    override fun incrementStatistic(p0: Statistic, p1: Int) = unsupported()
    override fun incrementStatistic(p0: Statistic, p1: Material) = unsupported()
    override fun incrementStatistic(p0: Statistic, p1: Material, p2: Int) = unsupported()
    override fun incrementStatistic(p0: Statistic, p1: EntityType) = unsupported()
    override fun incrementStatistic(p0: Statistic, p1: EntityType, p2: Int) = unsupported()

    override fun decrementStatistic(p0: Statistic) = unsupported()
    override fun decrementStatistic(p0: Statistic, p1: Int) = unsupported()
    override fun decrementStatistic(p0: Statistic, p1: Material) = unsupported()
    override fun decrementStatistic(p0: Statistic, p1: Material, p2: Int) = unsupported()
    override fun decrementStatistic(p0: Statistic, p1: EntityType) = unsupported()
    override fun decrementStatistic(p0: Statistic, p1: EntityType, p2: Int) = unsupported()

    override fun setStatistic(p0: Statistic, p1: Int) = unsupported()
    override fun setStatistic(p0: Statistic, p1: Material, p2: Int) = unsupported()
    override fun setStatistic(p0: Statistic, p1: EntityType, p2: Int) = unsupported()

    override fun getStatistic(p0: Statistic): Int = unsupported()
    override fun getStatistic(p0: Statistic, p1: Material): Int = unsupported()
    override fun getStatistic(p0: Statistic, p1: EntityType): Int = unsupported()

    override fun ban(p0: String?, p1: Date?, p2: String?): BanEntry<PlayerProfile>? = unsupported()
    override fun ban(p0: String?, p1: Instant?, p2: String?): BanEntry<PlayerProfile>? = unsupported()
    override fun ban(p0: String?, p1: Duration?, p2: String?): BanEntry<PlayerProfile>? = unsupported()
}
