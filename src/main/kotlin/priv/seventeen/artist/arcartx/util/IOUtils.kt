/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

/** IO 工具类 */
object IOUtils {

    @JvmStatic
    fun closeQuietly(closeable: AutoCloseable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: Exception) {
                // 静默关闭：closeQuietly 的语义即忽略关闭时的异常，无需上报
            }
        }
    }

    @JvmStatic
    fun closeQuietly(vararg closeables: AutoCloseable?) {
        for (autoCloseable in closeables) {
            closeQuietly(autoCloseable)
        }
    }
}