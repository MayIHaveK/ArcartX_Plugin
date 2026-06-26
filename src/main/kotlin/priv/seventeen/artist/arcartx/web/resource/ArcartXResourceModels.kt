/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.web.resource

/** 资源同步数据模型 */
data class ArcartXApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)

/** 资源同步数据模型 */
data class Crc64ListData(
    val files: List<FileInfo>,
    val totalCount: Int
)

/** 资源同步数据模型 */
data class FileInfo(
    val fileName: String,
    val crc64: String
)

/** 资源同步数据模型 */
data class SignedLinkRequest(
    val fileName: String,
    val expirationMinutes: Int = 30,
    val downloadLimit: Int = 3
)

/** 资源同步数据模型 */
data class SignedLinkData(
    val downloadUrl: String,
    val expiresAt: String,
    val downloadLimit: Int
)
