/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.web.resource.ArcartXResourceClient

/** 资源同步命令 */
class ResourceSyncCommand : MultiCommandExecutor() {
    override val name: String
        get() = "resourceSync"

    @CommandHandler(
        command = "update",
        description = "从后端同步文件校验信息",
        permission = "arcartx.command.admin.resource",
        args = [],
        senderType = SenderType.OP
    )
    fun update(context: CommandContext) {
        context.sendMessage(L(AXLanguageKey.REQUEST_IN_PROGRESS))
        ArcartXResourceClient.getInstance()?.let { client ->
            client.updateCrc64ListAsync(
                onSuccess = { updated ->
                    context.sendMessage(L(AXLanguageKey.RESOURCE_SYNC_COMPLETE, updated.size.toString()))
                },
                onFailure = { errorMessage ->
                    context.sendMessage(L(AXLanguageKey.RESOURCE_SYNC_FAILED, errorMessage.toString()))
                }
            )
        } ?: run {
            context.sendMessage(L(AXLanguageKey.RESOURCE_CLIENT_NOT_ENABLED))
        }

    }

}