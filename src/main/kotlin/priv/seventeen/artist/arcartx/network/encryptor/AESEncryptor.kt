/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.encryptor

import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.Key
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

/**
 * AES-GCM 对称加密器，用于客户端-服务端加密通信。
 */
class AESEncryptor {

    var aesKey: Key? = null

    @Throws(GeneralSecurityException::class)
    private fun encryptAES(input: ByteArray): ByteArray {
        val iv = ByteArray(GCM_IV_LENGTH)
        SECURE_RANDOM.nextBytes(iv)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, GCMParameterSpec(GCM_TAG_BITS, iv))
        return iv + cipher.doFinal(input)
    }

    @Throws(GeneralSecurityException::class)
    private fun decryptAES(input: ByteArray): ByteArray {
        if (input.size <= GCM_IV_LENGTH) throw GeneralSecurityException("密文长度不足")
        val iv = input.copyOfRange(0, GCM_IV_LENGTH)
        val cipherText = input.copyOfRange(GCM_IV_LENGTH, input.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, aesKey, GCMParameterSpec(GCM_TAG_BITS, iv))
        return cipher.doFinal(cipherText)
    }

    fun encode(data: String): String? {
        return try {
            parseByte2HexStr(encryptAES(data.toByteArray(StandardCharsets.UTF_8)))
        } catch (e: GeneralSecurityException) {
            null
        }
    }

    fun decode(data: String): String? {
        return try {
            val bytes = parseHexStr2Byte(data) ?: return null
            String(decryptAES(bytes), StandardCharsets.UTF_8)
        } catch (e: GeneralSecurityException) {
            null
        }
    }

    companion object {

        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_BITS = 128
        private val SECURE_RANDOM = SecureRandom()

        fun parseByte2HexStr(buf: ByteArray): String {
            val sb = StringBuilder()
            for (i in buf.indices) {
                var hex = Integer.toHexString(buf[i].toInt() and 0xFF)
                if (hex.length == 1) {
                    hex = "0$hex"
                }
                sb.append(hex.uppercase(Locale.getDefault()))
            }
            return sb.toString()
        }

        fun parseHexStr2Byte(hexStr: String): ByteArray? {
            if (hexStr.isEmpty()) return null
            val result = ByteArray(hexStr.length / 2)
            for (i in 0 until hexStr.length / 2) {
                val high = hexStr.substring(i * 2, i * 2 + 1).toInt(16)
                val low = hexStr.substring(i * 2 + 1, i * 2 + 2).toInt(16)
                result[i] = (high * 16 + low).toByte()
            }
            return result
        }
    }
}
