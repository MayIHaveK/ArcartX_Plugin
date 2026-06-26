/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.encryptor

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.crypto.KeyGenerator

class AESEncryptorTest {

    @Test
    fun `hex string round-trips`() {
        val bytes = byteArrayOf(0x00, 0x0F, 0x7F, -1, -128, 0x42)
        val hex = AESEncryptor.parseByte2HexStr(bytes)
        assertEquals("000F7FFF8042", hex)
        assertArrayEquals(bytes, AESEncryptor.parseHexStr2Byte(hex))
    }

    @Test
    fun `empty hex string parses to null`() {
        assertEquals(null, AESEncryptor.parseHexStr2Byte(""))
    }

    @Test
    fun `encode then decode round-trips`() {
        val key = KeyGenerator.getInstance("AES").apply { init(128) }.generateKey()
        val enc = AESEncryptor().apply { aesKey = key }
        val plain = "ArcartX 加密测试 123"
        val cipher = enc.encode(plain)
        assertEquals(plain, enc.decode(cipher!!))
    }
}
