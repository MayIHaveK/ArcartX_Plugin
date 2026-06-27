/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.web.resource

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/** 资源同步客户端 */
class ArcartXResourceClient {

    companion object {

        var client: ArcartXResourceClient? = null

        @Awake(LifeCycle.ACTIVE)
        fun init(){
            if(configs.setting.resourceSync.enable){
                bukkitPlugin.sendMessage(L(AXLanguageKey.RESOURCE_SERVICE_START))
                client = ArcartXResourceClient()
                // 异步更新CRC64列表
                client?.updateCrc64ListAsync(
                    onSuccess = { response ->
                        bukkitPlugin.sendMessage(L(AXLanguageKey.RESOURCE_FILE_SYNCED, response.size.toString()))
                    },
                    onFailure = { throwable ->
                        bukkitPlugin.sendMessage(L(AXLanguageKey.RESOURCE_FILE_SYNC_FAILED, throwable.message ?: ""))
                    }
                )
            }
        }

        @Awake(LifeCycle.DISABLE)
        fun shutdown() {
            client?.close()
            client = null
        }

        fun getInstance(): ArcartXResourceClient? = client
    }

    // API地址
    private val apiUrl: String = configs.setting.resourceSync.url

    // API密钥
    private val apiKey: String = configs.setting.resourceSync.apiKey

    // HTTP客户端
    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .build()

    // JSON序列化器
    private val gson = Gson()

    // 线程安全的缓存
    val cachedCrc64List: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    // 缓存更新互斥锁
    private val cacheLock = ReentrantLock()

    // 异步任务线程池（用于管理异步操作的生命周期，使用守护线程避免阻塞JVM退出）
    private val threadId = AtomicInteger(0)
    private val executor = Executors.newCachedThreadPool { runnable ->
        Thread(runnable, "ArcartX-Resource-${threadId.incrementAndGet()}").apply { isDaemon = true }
    }

    private fun updateCrc64List(): Result<Map<String, String>> {
        return try {
            // 构建请求
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${apiUrl.trimEnd('/')}/api/files/crc64-list"))
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .GET()
                .build()

            // 发送请求并处理响应（同步阻塞，运行在线程池中）
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() !in 200..299) {
                throw RuntimeException(L(AXLanguageKey.EXCEPTION_HTTP_REQUEST_FAILED, response.statusCode().toString()))
            }

            val responseBody = response.body()
            val typeToken = object : TypeToken<ArcartXApiResponse<Crc64ListData>>() {}
            val apiResponse = gson.fromJson<ArcartXApiResponse<Crc64ListData>>(responseBody, typeToken.type)

            if (!apiResponse.success) {
                throw RuntimeException(L(AXLanguageKey.EXCEPTION_API_ERROR, apiResponse.message ?: L(AXLanguageKey.EXCEPTION_UNKNOWN)))
            }

            val crc64Data = apiResponse.data ?: throw RuntimeException(L(AXLanguageKey.EXCEPTION_RESPONSE_EMPTY))

            // 更新缓存
            cacheLock.withLock {
                cachedCrc64List.clear()
                crc64Data.files.forEach { fileInfo ->
                    cachedCrc64List[fileInfo.fileName] = fileInfo.crc64
                }
            }

            Result.success(cachedCrc64List.toMap())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateSignedDownloadLink(
        fileName: String,
        expirationMinutes: Int = 15,
        downloadLimit: Int = 2
    ): Result<SignedLinkData> {
        return try {
            val requestBody = SignedLinkRequest(
                fileName = fileName,
                expirationMinutes = expirationMinutes,
                downloadLimit = downloadLimit
            )

            val jsonBody = gson.toJson(requestBody)

            val request = HttpRequest.newBuilder()
                .uri(URI.create("${apiUrl.trimEnd('/')}/api/files/generate-signed-link"))
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build()

            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() !in 200..299) {
                throw RuntimeException(L(AXLanguageKey.EXCEPTION_HTTP_REQUEST_FAILED, response.statusCode().toString()))
            }

            val responseBody = response.body()
            val typeToken = object : TypeToken<ArcartXApiResponse<SignedLinkData>>() {}
            val apiResponse = gson.fromJson<ArcartXApiResponse<SignedLinkData>>(responseBody, typeToken.type)

            if (!apiResponse.success) {
                throw RuntimeException(L(AXLanguageKey.EXCEPTION_API_ERROR, apiResponse.message ?: L(AXLanguageKey.EXCEPTION_UNKNOWN)))
            }

            val signedLinkData = apiResponse.data ?: throw RuntimeException(L(AXLanguageKey.EXCEPTION_RESPONSE_EMPTY))
            Result.success(signedLinkData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateCrc64ListAsync(
        onSuccess: (Map<String, String>) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        executor.execute {
            try {
                val result = updateCrc64List()
                if (result.isSuccess) {
                    onSuccess(result.getOrThrow())
                } else {
                    onFailure(result.exceptionOrNull() ?: RuntimeException(L(AXLanguageKey.EXCEPTION_UNKNOWN)))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun generateSignedDownloadLinkAsync(
        fileName: String,
        expirationMinutes: Int = 15,
        downloadLimit: Int = 2,
        onSuccess: (SignedLinkData) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        executor.execute {
            try {
                val result = generateSignedDownloadLink(fileName, expirationMinutes, downloadLimit)
                if (result.isSuccess) {
                    onSuccess(result.getOrThrow())
                } else {
                    onFailure(result.exceptionOrNull() ?: RuntimeException(L(AXLanguageKey.EXCEPTION_UNKNOWN)))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun generateSignedDownloadLinkAsync(
        files: List<String>,
        expirationMinutes: Int = 15,
        downloadLimit: Int = 2,
        callback: (MutableMap<String, String>) -> Unit
    ) {
        executor.execute {
            try {
                val results = mutableMapOf<String, String>()
                for(name in files){
                    val result = generateSignedDownloadLink(fileName = name, expirationMinutes, downloadLimit)
                    if (result.isSuccess) {
                        results[name] = apiUrl + result.getOrThrow().downloadUrl
                    }
                }
                callback(results)
            } catch (e: Exception) {
                callback(mutableMapOf())
            }
        }
    }

    fun close() {
        executor.shutdownNow()
    }
}
