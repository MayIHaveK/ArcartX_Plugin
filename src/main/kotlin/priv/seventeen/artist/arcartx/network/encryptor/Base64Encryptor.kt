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
import java.util.*

/** Base64 编解码工具 */
object Base64Encryptor {

    fun base64Encrypt(data: String): String {
        return String(Base64.getEncoder().encode(data.toByteArray(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
    }

    fun base64Encrypt(data: ByteArray?): String {
        return String(Base64.getEncoder().encode(data), StandardCharsets.UTF_8)
    }

    fun base64Decrypt(data: String?): String {
        val resultBytes = Base64.getDecoder().decode(data)
        return String(resultBytes, StandardCharsets.UTF_8)
    }

    fun base64DecryptByte(data: String?): ByteArray {
        return Base64.getDecoder().decode(data)
    }

}