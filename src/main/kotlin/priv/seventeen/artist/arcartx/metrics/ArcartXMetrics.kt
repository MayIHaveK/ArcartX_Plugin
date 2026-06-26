/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.metrics

import org.bstats.bukkit.Metrics
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.lifecycle.Awake
import priv.seventeen.artist.blink.lifecycle.LifeCycle

/**
 * bStats 数据统计接入。
 *
 * 仪表盘：https://bstats.org/plugin/bukkit/ArcartX/24698
 *
 * `org.bstats` 在 shadowJar 阶段被 relocate 进 `priv.seventeen.artist.arcartx.bstats`，
 * 此处仍按原包名编译，由 shadow 在打包时同步重写引用。
 */
object ArcartXMetrics {

    private const val SERVICE_ID = 24698

    private var metrics: Metrics? = null

    @Awake(LifeCycle.ENABLE)
    fun start() {
        metrics = Metrics(bukkitPlugin, SERVICE_ID)
    }

    @Awake(LifeCycle.DISABLE)
    fun stop() {
        // bStats 自带独立的 ScheduledExecutorService，reload 时若不关闭会导致旧 ClassLoader 无法回收
        metrics?.shutdown()
        metrics = null
    }
}
