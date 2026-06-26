/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.command

import org.bukkit.Material
import org.bukkit.entity.Player
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.util.ItemStackUtils.addExtraModel
import priv.seventeen.artist.arcartx.util.ItemStackUtils.addLore
import priv.seventeen.artist.arcartx.util.ItemStackUtils.insertLore
import priv.seventeen.artist.arcartx.util.ItemStackUtils.removeLore
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setCoolDownTag
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setDisplayName
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setDrop
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setFPModel
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setIcon
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setLore
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setModel
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setUrl
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.asteroid.item.ItemTagData

/** 物品编辑命令 */
class ItemEditCommand : MultiCommandExecutor() {
    override val name: String
        get() = "item"

    @CommandHandler(
        command = "addTag",
        description = "添加NBT",
        permission = "arcartx.command.admin.item.edit",
        args = ["key","value"],
        senderType = SenderType.PLAYER
    )
    fun addNBT(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.itemMeta = ItemTag.fromItemStack(item).apply {
            this[context.getArg(0)] = ItemTagData.of(context.getArg(1))
        }.saveTo(item).itemMeta
    }

    @CommandHandler(
        command = "addLore",
        description = "添加lore",
        permission = "arcartx.command.admin.item.edit",
        args = ["lore"],
        senderType = SenderType.PLAYER
    )
    fun addLore(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.addLore(context.getArg(0))
    }

    @CommandHandler(
        command = "setLore",
        description = "设置lore",
        permission = "arcartx.command.admin.item.edit",
        args = ["lore", "line"],
        senderType = SenderType.PLAYER
    )
    fun setLore(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        val line = context.getArgAsInt(1) ?: return
        item.setLore(context.getArg(0), line)
    }


    @CommandHandler(
        command = "insertLore",
        description = "插入lore",
        permission = "arcartx.command.admin.item.edit",
        args = ["lore", "line"],
        senderType = SenderType.PLAYER
    )
    fun insertLore(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        val line = context.getArgAsInt(1) ?: return
        item.insertLore( context.getArg(0), line)
    }

    @CommandHandler(
        command = "removeLore",
        description = "删除lore",
        permission = "arcartx.command.admin.item.edit",
        args = ["line"],
        senderType = SenderType.PLAYER
    )
    fun removeLore(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        val line = context.getArgAsInt(0) ?: return
        item.removeLore(line)
    }

    @CommandHandler(
        command = "setDisplayName",
        description = "设置名称",
        permission = "arcartx.command.admin.item.edit",
        args = ["name"],
        senderType = SenderType.PLAYER
    )
    fun setDisplayName(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.setDisplayName( context.getArg(0))
    }

    @CommandHandler(
        command = "setIcon",
        description = "设置图标",
        permission = "arcartx.command.admin.item.edit",
        args = ["path"],
        senderType = SenderType.PLAYER
    )
    fun setIcon(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.setIcon(context.getArg(0))
    }

    @CommandHandler(
        command = "setModel",
        description = "设置模型",
        permission = "arcartx.command.admin.item.edit",
        args = ["modelID"],
        senderType = SenderType.PLAYER
    )
    fun setModel(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.setModel(context.getArg(0))
    }

    @CommandHandler(
        command = "setFPModel",
        description = "设置第一人称模型",
        permission = "arcartx.command.admin.item.edit",
        args = ["modelID"],
        senderType = SenderType.PLAYER
    )
    fun setFPModel(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.setFPModel(context.getArg(0))
    }

    @CommandHandler(
        command = "setExtraModel",
        description = "设置额外渲染模型",
        permission = "arcartx.command.admin.item.edit",
        args = ["locator", "modelID"],
        senderType = SenderType.PLAYER
    )
    fun setExtraModel(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.addExtraModel(context.getArg(0), context.getArg(1))
    }

    @CommandHandler(
        command = "setUrl",
        description = "设置界面显示贴图",
        permission = "arcartx.command.admin.item.edit",
        args = ["modelID"],
        senderType = SenderType.PLAYER
    )
    fun setUrl(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.setUrl(context.getArg(0))
    }

    @CommandHandler(
        command = "setDrop",
        description = "设置掉落附加模型",
        permission = "arcartx.command.admin.item.edit",
        args = ["modelID"],
        senderType = SenderType.PLAYER
    )
    fun setDrop(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.setDrop(context.getArg(0))
    }

    @CommandHandler(
        command = "setCoolDownTag",
        description = "设置冷却标签",
        permission = "arcartx.command.admin.item.edit",
        args = ["value"],
        senderType = SenderType.PLAYER
    )
    fun setCoolDownTag(context: CommandContext) {
        val player = context.getSender() as Player
        val item = player.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            context.sendMessage(L(AXLanguageKey.HOLD_ITEM_REQUIRED))
            return
        }
        item.setCoolDownTag(context.getArg(0))
    }
}