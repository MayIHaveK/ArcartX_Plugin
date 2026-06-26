/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.encryptor

import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement
import javax.crypto.SecretKey


/**
 * 有限域 DH 密钥协商（2048 位），用于在服务端与客户端 Mod 间派生会话 AES 密钥。
 *
 * 安全边界说明：本协议层提供机密性与防篡改，但 **DH 公钥交换未做身份认证**（双方交换裸公钥，
 * 无签名/证书锁定）。它依赖底层 Minecraft 连接完成端点认证——online-mode 下整条链路（含插件消息）
 * 已被 MC 协议加密 + 正版会话认证覆盖，本层属纵深防御。在 offline-mode 或代理后端明文腿上，
 * 理论上存在中间人替换公钥的风险。请勿将本层视为独立的认证安全边界，威胁模型与部署建议见 SECURITY.md。
 */
object DHEncryptor {

    fun generateKeyPair(): KeyPair? {
        return try {
            val keyPairGenerator = KeyPairGenerator.getInstance("DH")
            // 2048 位有限域 DH（NIST 已弃用 <2048）；客户端从服务端公钥读取参数，自动适配位数
            keyPairGenerator.initialize(2048)
            keyPairGenerator.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }


    fun getSecretKey(publicKey: ByteArray?, privateKey: ByteArray?): SecretKey {
        try {
            System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true")
            val keyFactory = KeyFactory.getInstance("DH")
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKey))
            val priKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKey))
            val keyAgreement = KeyAgreement.getInstance("DH")
            keyAgreement.init(priKey)
            keyAgreement.doPhase(pubKey, true)
            return keyAgreement.generateSecret("AES")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}