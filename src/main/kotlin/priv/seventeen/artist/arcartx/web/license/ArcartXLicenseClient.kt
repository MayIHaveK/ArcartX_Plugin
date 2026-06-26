/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.web.license

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import priv.seventeen.artist.arcartx.ArcartX.configs
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.lifecycle.Awake
import priv.seventeen.artist.blink.lifecycle.LifeCycle
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture

/** 许可证验证客户端 */
class ArcartXLicenseClient {
    companion object {
        @Awake(LifeCycle.ACTIVE)
        fun init() {
            // 使用许可证ID和密钥进行验证
            ArcartXLicenseClient().validateLicense(configs.setting.licenseId, configs.setting.licenseKey)
        }
    }

    private val host: String = "api.arcartx.com"
    private val port: Int = 443
    private val gson = Gson()
    private var httpClient: HttpClient? = null
    private val licenseValidatePath: String = "/api/license/validate"

    init {
        this.httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
    }

    private fun <T> sendRequest(
        method: String,
        path: String,
        body: String?,
        responseClass: Class<T>
    ): CompletableFuture<T> {
        val requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create("https://$host:$port$path"))
            .timeout(Duration.ofMinutes(1))
            .header("Content-Type", "application/json")
        // 设置请求方法和请求体
        val request = when (method.uppercase(Locale.getDefault())) {
            "POST" -> requestBuilder.POST(if (body != null) HttpRequest.BodyPublishers.ofString(body) else HttpRequest.BodyPublishers.noBody())
                .build()
            "PUT" -> requestBuilder.PUT(if (body != null) HttpRequest.BodyPublishers.ofString(body) else HttpRequest.BodyPublishers.noBody())
                .build()
            else -> requestBuilder.GET().build()
        }
        return httpClient!!.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply { response: HttpResponse<String?> ->
                when (response.statusCode()) {
                    200 -> parseResponse(response.body(), responseClass)
                    400 -> {
                        // 处理400错误，尝试解析错误响应
                        val errorResponse = parseResponse(response.body(), LicenseErrorResponse::class.java)
                        throw LicenseValidationException(errorResponse?.message ?: "许可证验证失败")
                    }
                    else -> throw LicenseValidationException("HTTP请求失败，状态码: ${response.statusCode()}")
                }
            }
    }

    private fun <T> parseResponse(content: String?, clazz: Class<T>): T {
        return gson.fromJson(content, clazz)
    }

    fun validateLicense(licenseId: Int, licenseKey: String) {
        val request = LicenseValidateRequest(licenseId, licenseKey)
        // 如果为空直接失败
        if (licenseId == 0 || licenseKey.isEmpty()) {
            sendLicenseFailMessage("未填写许可信息")
            return
        }
        sendRequest(
            "POST",
            licenseValidatePath,
            gson.toJson(request),
            LicenseValidateResponse::class.java
        ).thenAccept { response ->
            if (response.success) {
                sendLicenseSuccessMessage(response)
            } else {
                sendLicenseFailMessage(response.message ?: "许可证验证失败")
            }
        }.exceptionally { throwable ->
            when (throwable) {
                is LicenseValidationException -> sendLicenseFailMessage(throwable.message ?: "许可证验证失败")
                else -> sendLicenseFailMessage("网络错误: ${throwable.message}")
            }
            null
        }
    }

    private fun sendLicenseSuccessMessage(response: LicenseValidateResponse) {
        val licenseInfo = response.licenseInfo
        bukkitPlugin.sendMessage(L(AXLanguageKey.LICENSE_WELCOME, licenseInfo?.username ?: ""))
    }

    private fun sendLicenseFailMessage(message: String) {
        bukkitPlugin.sendMessage(L(AXLanguageKey.LICENSE_VALIDATE_FAILED, message))
        bukkitPlugin.sendMessage(L(AXLanguageKey.LICENSE_INFO_NOTE))
        bukkitPlugin.sendMessage(L(AXLanguageKey.LICENSE_EULA_REQUIRED))
        bukkitPlugin.sendMessage(L(AXLanguageKey.LICENSE_EULA_LINK))
    }


    data class LicenseValidateRequest(
        @field:SerializedName("licenseId") val licenseId: Int,
        @field:SerializedName("licenseKey") val licenseKey: String,
        @field:SerializedName("resourceId") val resourceId: Int = 6,
        @field:SerializedName("clientIp") val clientIp: String = "0.0.0.0"
    )

    data class LicenseValidateResponse(
        @field:SerializedName("success") val success: Boolean,
        @field:SerializedName("message") val message: String?,
        @field:SerializedName("remainingTime") val remainingTime: String?,
        @field:SerializedName("licenseInfo") val licenseInfo: LicenseInfo?
    )

    data class LicenseInfo(
        @field:SerializedName("licenseId") val licenseId: Int,
        @field:SerializedName("resourceId") val resourceId: Int,
        @field:SerializedName("userId") val userId: Int,
        @field:SerializedName("username") val username: String,
        @field:SerializedName("validated") val validated: Boolean,
        @field:SerializedName("active") val active: Boolean,
        @field:SerializedName("startDate") val startDate: Long,
        @field:SerializedName("endDate") val endDate: Long,
        @field:SerializedName("boundIp") val boundIp: String
    )

    data class LicenseErrorResponse(
        @field:SerializedName("success") val success: Boolean,
        @field:SerializedName("message") val message: String?
    )

    // 自定义异常类
    class LicenseValidationException(message: String) : Exception(message)
}
