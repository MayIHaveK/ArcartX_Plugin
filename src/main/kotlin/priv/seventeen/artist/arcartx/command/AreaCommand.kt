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
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.util.ChatUtils.replaceColorChar
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setCoolDownTag
import priv.seventeen.artist.arcartx.util.ItemStackUtils.setDisplayName
import priv.seventeen.artist.asteroid.item.ItemTag
import priv.seventeen.artist.asteroid.item.ItemTagData

/** 区域管理命令 */
class AreaCommand : MultiCommandExecutor() {

    override val name: String
        get() = "area"

    @CommandHandler(
        command = "tool",
        description = "获取区域工具",
        permission = "arcartx.command.admin.area",
        args = [],
        senderType = SenderType.PLAYER
    )
    fun tool(context: CommandContext) {
        (context.getSender() as Player).let {
            it.inventory.addItem(ItemStack(Material.STICK).also { item ->
                item.setDisplayName("&bArc&3art&1X&f区域工具")
                item.itemMeta = ItemTag.fromItemStack(item).apply { this["tool"] = ItemTagData.of("area") }.saveTo(item).itemMeta?.apply {
                    lore = listOf(
                        "&b左键 + 右键 &f选择区域两端对角".replaceColorChar(),
                        "&bALT + 右键 &f删除区域".replaceColorChar(),
                        "&bALT + 左键 &f取消选择".replaceColorChar(),
                        "&bShift + 右键 &f创建区域".replaceColorChar(),
                        "&bShift + 左键 &f编辑所在区域优先级".replaceColorChar(),
                    )
                }
                item.itemMeta?.addEnchant(Enchantment.ARROW_INFINITE,1,false)
                item.setCoolDownTag("area_tool")
            })
        }
    }

}