/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.util

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ByteArrayUtilsTest {

    @Test
    fun `compress then decompress round-trips`() {
        val original = ("ArcartX-" + "x".repeat(2000)).toByteArray()
        val compressed = ByteArrayUtils.compressIfNeeded(original)
        val restored = ByteArrayUtils.decompress(compressed)
        assertArrayEquals(original, restored)
    }

    @Test
    fun `small payload stays uncompressed and decompress is a no-op`() {
        val small = "hi".toByteArray()
        // 未达压缩阈值，原样返回
        assertArrayEquals(small, ByteArrayUtils.compressIfNeeded(small))
        // 无 gzip 魔数，decompress 原样返回
        assertArrayEquals(small, ByteArrayUtils.decompress(small))
    }

    @Test
    fun `decompression bomb with oversized declared size is rejected`() {
        // 伪造 gzip 魔数 + 尾部声明 0x7FFFFFFF 的超大原始长度，应被上限校验拦截
        val bomb = ByteArray(20)
        bomb[0] = 0x1f.toByte(); bomb[1] = 0x8b.toByte()
        bomb[16] = 0xff.toByte(); bomb[17] = 0xff.toByte()
        bomb[18] = 0xff.toByte(); bomb[19] = 0x7f.toByte()
        assertThrows(IllegalArgumentException::class.java) {
            ByteArrayUtils.decompress(bomb)
        }
    }
}
