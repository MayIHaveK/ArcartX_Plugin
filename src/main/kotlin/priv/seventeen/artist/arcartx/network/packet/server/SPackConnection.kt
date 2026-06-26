/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.network.packet.server

import com.google.gson.annotations.SerializedName
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.network.encryptor.Base64Encryptor
import priv.seventeen.artist.arcartx.network.encryptor.DHEncryptor
import java.security.KeyPair

class SPackConnection(player: Player) : ServerPacket {

    @SerializedName("message")
    private val message: String

    init {
        val keyPair: KeyPair? = DHEncryptor.generateKeyPair()
        ArcartXEntityManager.getPlayer(player)?.key = Base64Encryptor.base64Encrypt(keyPair!!.private.encoded)
        this.message = Base64Encryptor.base64Encrypt(keyPair.public.encoded)
    }

}