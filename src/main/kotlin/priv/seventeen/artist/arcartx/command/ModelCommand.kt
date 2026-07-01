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
import priv.seventeen.artist.arcartx.core.entity.ArcartXEntityManager
import priv.seventeen.artist.arcartx.core.entity.data.ArcartXPlayer
import priv.seventeen.artist.arcartx.commons.command.CommandContext
import priv.seventeen.artist.arcartx.commons.command.annotation.CommandHandler
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.network.NetworkMessageSender

/** 模型管理命令 */
class ModelCommand : MultiCommandExecutor() {

    override val name: String
        get() = "model"

    @CommandHandler(
        command = "setModel",
        description = "设置玩家模型",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "modelID", "scale"],
        senderType = SenderType.OP
    )
    fun setModel(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val scale = context.getArgAsDouble(2) ?: return
            ArcartXEntityManager.getPlayer(it)?.setModel(context.getArg(1), scale)
        }
    }

    @CommandHandler(
        command = "removeModel",
        description = "移除玩家模型",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player"],
        senderType = SenderType.OP
    )
    fun removeModel(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            ArcartXEntityManager.getPlayer(it)?.removeModel()
        }
    }

    @CommandHandler(
        command = "defaultModel",
        description = "设置玩家为内置默认模型(叠加盔甲/披风/鞘翅/时装)",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "scale"],
        senderType = SenderType.OP
    )
    fun defaultModel(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val scale = context.getArgAsDouble(1) ?: 1.0
            ArcartXEntityManager.getPlayer(it)?.setDefaultModel(scale)
        }
    }

    @CommandHandler(
        command = "customModel",
        description = "设置玩家模型",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "modelID", "scale"],
        senderType = SenderType.OP
    )
    fun customModel(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val scale = context.getArgAsDouble(2) ?: 1.0
            ArcartXEntityManager.getPlayer(it)?.setCustomModel(context.getArg(1), scale)
        }
    }


    @CommandHandler(
        command = "animationPack",
        description = "指派玩家动画包",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "packID"],
        senderType = SenderType.OP
    )
    fun animationPack(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            ArcartXEntityManager.getPlayer(it)?.setAnimationPack(context.getArg(1))
        }
    }

    @CommandHandler(
        command = "clearAnimationPack",
        description = "取消玩家动画包",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player"],
        senderType = SenderType.OP
    )
    fun clearAnimationPack(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            ArcartXEntityManager.getPlayer(it)?.clearAnimationPack()
        }
    }

    @CommandHandler(
        command = "testController",
        description = "测试动作控制器",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "controllerID"],
        senderType = SenderType.OP
    )
    fun testController(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            ArcartXEntityManager.getPlayer(it)?.setController(context.getArg(1))
        }
    }

    @CommandHandler(
        command = "testControllerState",
        description = "测试设置动作控制器状态",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "subControllerID","state","speed"],
        senderType = SenderType.OP
    )
    fun testControllerState(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val speed = context.getArgAsDouble(3) ?: return
            ArcartXEntityManager.getPlayer(it)?.setState(context.getArg(1), context.getArg(2), speed)
        }
    }

    @CommandHandler(
        command = "testAddExtraModel",
        description = "测试添加额外模型",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "locatorName","modelID"],
        senderType = SenderType.OP
    )
    fun testAddExtraModel(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            ArcartXEntityManager.getPlayer(it)?.addExtraModel(context.getArg(1), context.getArg(2))
        }
    }

    @CommandHandler(
        command = "testRemoveModel",
        description = "测试移除额外模型",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "locatorName"],
        senderType = SenderType.OP
    )
    fun testRemoveExtraModel(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            ArcartXEntityManager.getPlayer(it)?.removeExtraModel(context.getArg(1))
        }
    }



    @CommandHandler(
        command = "testAnimation",
        description = "测试动画",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "animationID","speed"],
        senderType = SenderType.OP
    )
    fun testAnimation(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val speed = context.getArgAsDouble(2) ?: return
            ArcartXEntityManager.getPlayer(it)?.playAnimation(context.getArg(1), speed, 5, -1)
        }
    }

    @CommandHandler(
        command = "testFPAnimationByCount",
        description = "测试第一人称动画(次数)",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "animationID","speed", "count"],
        senderType = SenderType.OP
    )
    fun testFPAnimationCount(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val speed = context.getArgAsDouble(2) ?: return
            val count = context.getArgAsInt(3) ?: return
            ArcartXEntityManager.getPlayer(it)?.playFirstPersonAnimationByCountOf(context.getArg(1), speed, count)
        }
    }


    @CommandHandler(
        command = "testFPAnimationByTime",
        description = "测试第一人称动画(持续时间)",
        permission = "arcartx.command.admin.entity.edit",
        args = ["player", "animationID","speed", "time"],
        senderType = SenderType.OP
    )
    fun testFPAnimationTime(context: CommandContext) {
        context.getArgAsPlayer(0)?.let {
            val speed = context.getArgAsDouble(2) ?: return
            val time = context.getArgAsInt(3) ?: return
            ArcartXEntityManager.getPlayer(it)?.playFirstPersonAnimationByTime(context.getArg(1), speed, time)
        }
    }

    @CommandHandler(
        command = "testAnimationUUID",
        description = "测试动画",
        permission = "arcartx.command.admin.entity.edit",
        args = ["uuid", "animationID","speed"],
        senderType = SenderType.OP
    )
    fun testAnimationUUID(context: CommandContext) {
        if(context.getSender() is Player){
            val player = context.getSender() as Player
            context.getArgAsUUID(0)?.let {
                val speed = context.getArgAsDouble(2) ?: return
                NetworkMessageSender.sendEntityAnimation(player, it, context.getArg(1), speed, 5, -1)
            }
        }

    }

    @CommandHandler(
        command = "testBlockAnimation",
        description = "测试方块动画",
        permission = "arcartx.command.admin.block.edit",
        args = ["x","y","z", "animationID","speed"],
        senderType = SenderType.PLAYER
    )
    fun testBlockAnimation(context: CommandContext) {
        val player = context.getSender() as Player
        val x = context.getArgAsInt(0) ?: return
        val y = context.getArgAsInt(1) ?: return
        val z = context.getArgAsInt(2) ?: return
        val speed = context.getArgAsDouble(4) ?: return
        NetworkMessageSender.sendBlockAnimation(
            player,
            x,
            y,
            z,
            context.getArg(3),
            speed,
            5,
            -1
        )
    }


}