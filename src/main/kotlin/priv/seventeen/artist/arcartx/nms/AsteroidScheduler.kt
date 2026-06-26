/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.nms

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.Plugin
import priv.seventeen.artist.asteroid.util.FoliaScheduler
import priv.seventeen.artist.asteroid.util.PaperCompat
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

/** 调度器桥接，封装 Asteroid FoliaScheduler 提供 Folia 兼容调度 */
object AsteroidScheduler {

    // ==================== 平台检测 ====================

    fun isPaper(): Boolean = PaperCompat.isPaper()

    fun isFolia(): Boolean = PaperCompat.isFolia()

    fun isSpigot(): Boolean = PaperCompat.isSpigot()

    // ==================== 任务句柄登记（用于 onDisable 统一取消，防止 Folia 下任务泄漏） ====================

    private val activeTasks: MutableSet<Any> = Collections.newSetFromMap(ConcurrentHashMap())

    /** 调度一次性任务并登记句柄；任务执行完毕后自动注销，避免登记表随一次性任务无限增长 */
    private fun trackOneShot(schedule: (Runnable) -> Any?, task: Runnable): Any? {
        val ref = AtomicReference<Any?>()
        val handle = schedule(Runnable {
            try {
                task.run()
            } finally {
                ref.get()?.let { activeTasks.remove(it) }
            }
        })
        if (handle != null) {
            activeTasks.add(handle)
            ref.set(handle)
        }
        return handle
    }

    /** 调度周期任务并登记句柄；周期任务长期存活，直到被取消或 cancelAll */
    private fun trackTimer(handle: Any?): Any? {
        if (handle != null) activeTasks.add(handle)
        return handle
    }

    /** 取消所有经本调度器登记且仍在追踪的任务*/
    fun cancelAll() {
        val snapshot = activeTasks.toList()
        activeTasks.clear()
        snapshot.forEach { runCatching { FoliaScheduler.cancelTask(it) } }
    }

    // ==================== 同步调度 ====================

    fun runTask(plugin: Plugin, task: Runnable): Any? =
        trackOneShot({ FoliaScheduler.runTask(plugin, it) }, task)

    fun runTaskLater(plugin: Plugin, task: Runnable, delayTicks: Long): Any? =
        trackOneShot({ FoliaScheduler.runTaskLater(plugin, it, delayTicks) }, task)

    fun runTaskTimer(plugin: Plugin, task: Runnable, delayTicks: Long, periodTicks: Long): Any? =
        trackTimer(FoliaScheduler.runTaskTimer(plugin, task, delayTicks, periodTicks))

    // ==================== 异步调度 ====================

    fun runAsync(plugin: Plugin, task: Runnable): Any? =
        trackOneShot({ FoliaScheduler.runAsync(plugin, it) }, task)

    fun runAsyncLater(plugin: Plugin, task: Runnable, delayTicks: Long): Any? =
        trackOneShot({ FoliaScheduler.runAsyncLater(plugin, it, delayTicks) }, task)

    fun runAsyncTimer(plugin: Plugin, task: Runnable, delayTicks: Long, periodTicks: Long): Any? =
        trackTimer(FoliaScheduler.runAsyncTimer(plugin, task, delayTicks, periodTicks))

    // ==================== 区域安全调度（Folia） ====================

    fun runEntityTask(plugin: Plugin, entity: Entity, task: Runnable): Any? =
        trackOneShot({ FoliaScheduler.runEntityTask(plugin, entity, it) }, task)

    fun runLocationTask(plugin: Plugin, location: Location, task: Runnable): Any? =
        trackOneShot({ FoliaScheduler.runLocationTask(plugin, location, it) }, task)

    // ==================== 任务取消 ====================

    fun cancelTask(handle: Any?) {
        if (handle != null) activeTasks.remove(handle)
        FoliaScheduler.cancelTask(handle)
    }

    // ==================== 线程保障 ====================

    fun ensureMainThread(plugin: Plugin, task: Runnable) {
        if (isFolia()) {
            runTask(plugin, task)
        } else {
            if (Bukkit.isPrimaryThread()) {
                task.run()
            } else {
                runTask(plugin, task)
            }
        }
    }

    fun ensureAsyncThread(plugin: Plugin, task: Runnable) {
        if (Bukkit.isPrimaryThread()) {
            runAsync(plugin, task)
        } else {
            task.run()
        }
    }

    // ==================== Kotlin Plugin 扩展 ====================

    @JvmName("pluginRunTask")
    fun Plugin.runTask(task: Runnable): Any? = runTask(this, task)

    @JvmName("pluginRunTaskLater")
    fun Plugin.runTaskLater(task: Runnable, delayTicks: Long): Any? = runTaskLater(this, task, delayTicks)

    @JvmName("pluginRunTaskTimer")
    fun Plugin.runTaskTimer(task: Runnable, delayTicks: Long, periodTicks: Long): Any? = runTaskTimer(this, task, delayTicks, periodTicks)

    @JvmName("pluginRunAsync")
    fun Plugin.runAsync(task: Runnable): Any? = runAsync(this, task)

    @JvmName("pluginRunAsyncLater")
    fun Plugin.runAsyncLater(task: Runnable, delayTicks: Long): Any? = runAsyncLater(this, task, delayTicks)

    @JvmName("pluginRunAsyncTimer")
    fun Plugin.runAsyncTimer(task: Runnable, delayTicks: Long, periodTicks: Long): Any? = runAsyncTimer(this, task, delayTicks, periodTicks)

    @JvmName("pluginRunEntityTask")
    fun Plugin.runEntityTask(entity: Entity, task: Runnable): Any? = runEntityTask(this, entity, task)

    @JvmName("pluginRunLocationTask")
    fun Plugin.runLocationTask(location: Location, task: Runnable): Any? = runLocationTask(this, location, task)

    @JvmName("pluginEnsureMainThread")
    fun Plugin.ensureMainThread(task: Runnable) = ensureMainThread(this, task)

    @JvmName("pluginEnsureAsyncThread")
    fun Plugin.ensureAsyncThread(task: Runnable) = ensureAsyncThread(this, task)
}
