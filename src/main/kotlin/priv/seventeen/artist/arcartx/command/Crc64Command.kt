/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.ArcartX
import priv.seventeen.artist.arcartx.core.config.setting.Setting
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L

/** CRC64 校验命令 */
class Crc64Command : MultiCommandExecutor() {
    override val name: String
        get() = "crc64"

    @CommandHandler(
        command = "update",
        description = "更新客户端拓展校验crc64列表",
        permission = "arcartx.command.admin.crc64",
        args = [],
        senderType = SenderType.PLAYER
    )
    fun update(context: CommandContext) {
        val player = context.getSender() as Player
        val crc64 = ArcartXEntityManager.getPlayer(player)?.crc64;
        val setting: Setting = ArcartX.configs.setting
        crc64?.let {
            setting.crc64Setting.list.clear()
            setting.crc64Setting.list.addAll(it)
        }
        setting.configFile?.delete().apply {
            setting.load()
            context.sendMessage(L(AXLanguageKey.CRC_UPDATE_SUCCESS))
        } ?: context.sendMessage(L(AXLanguageKey.CRC_UPDATE_FAILED))

    }
}