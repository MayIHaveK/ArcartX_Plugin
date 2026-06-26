/*
 * Copyright (c) 2026 17Artist
 *
 * This file is part of ArcartX, licensed under the ArcartX Source-Available
 * License 1.0. Use of this software requires acceptance of the ArcartX EULA:
 * https://arcartx.com/resources/eula/view
 * See the LICENSE file for full terms. Provided "AS IS", without warranty.
 */

package priv.seventeen.artist.arcartx.commons.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import priv.seventeen.artist.arcartx.commons.command.annotation.SenderType
import priv.seventeen.artist.arcartx.commons.command.executor.AXCommandExecutor
import priv.seventeen.artist.arcartx.commons.command.executor.MultiCommandExecutor
import priv.seventeen.artist.arcartx.commons.command.executor.SingleCommandExecutor
import priv.seventeen.artist.arcartx.language.AXLanguageKey
import priv.seventeen.artist.arcartx.language.L
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendException
import priv.seventeen.artist.arcartx.commons.message.ArcartXSender.Companion.sendMessage
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors

/** 命令处理器 */
class AXCommand(name: String, vararg aliases: String) :
    Command(name, "", "/$name", ArrayList(listOf(*aliases))) {
    private val subCommands: MutableMap<String, AXCommandExecutor> = LinkedHashMap<String, AXCommandExecutor>()
    private val tabCompleteMap: MutableMap<String, Supplier<Set<String>>> = HashMap()

    private val names: MutableMap<String, String> = HashMap()
    private var plugin: JavaPlugin? = null

    fun setPlugin(plugin: JavaPlugin?) {
        this.plugin = plugin
    }

    fun registerSubCommand(executor: AXCommandExecutor) {
        subCommands[executor.name] = executor
        names[executor.name.lowercase(Locale.ROOT)] = executor.name
    }

    /**
     * 为参数键注册 Tab 补全候选来源。argKey 对应命令首个参数的描述键（见 argsDescription），
     * 例如为 "model" 注册返回所有模型 ID 的 supplier，即可让该参数获得补全。
     * 在此之前 tabCompleteMap 无填充入口，参数级补全形同虚设。
     */
    fun registerTabCompleter(argKey: String, supplier: Supplier<Set<String>>) {
        tabCompleteMap[argKey] = supplier
    }


    @Throws(IllegalArgumentException::class)
    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            return filterByPrefix(subCommands.keys, args[0])
        }
        if (args.size == 2) {
            val commandExecutor: AXCommandExecutor? = subCommands[names[args[0].lowercase()]]
            if (commandExecutor is MultiCommandExecutor) {
                return filterByPrefix(commandExecutor.functionMap.keys, args[1])
            }
            if (commandExecutor is SingleCommandExecutor) {
                if(commandExecutor.argsDescription.isEmpty()){
                    return super.tabComplete(sender, alias, args)
                }
                val argKey: String = commandExecutor.argsDescription[0]
                if (argKey.equals("player", ignoreCase = true)) {
                    return super.tabComplete(sender, alias, args)
                }
                if (tabCompleteMap[argKey] != null) {
                    return filterByPrefix(tabCompleteMap[argKey]!!.get(), args[1])
                }
            }
            return super.tabComplete(sender, alias, args)
        }


        return super.tabComplete(sender, alias, args)
    }


    private fun filterByPrefix(keys: Set<String>, prefix: String): List<String> {
        return keys.stream()
            .filter { key: String? -> key!!.lowercase().startsWith(prefix.lowercase(Locale.getDefault())) }
            .collect(Collectors.toList())
    }

    override fun execute(commandSender: CommandSender, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendCommandList(commandSender, label)
            return true
        }

        val commandExecutor: AXCommandExecutor? = subCommands[names[args[0].lowercase()]]
        if (commandExecutor == null) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.COMMAND_NOT_FOUND, label, args[0]))
            return true
        }

        val target = getTargetFunction(commandExecutor, commandSender, label, args) ?: return true
        val targetArgs = if (commandExecutor is MultiCommandExecutor) {
            Arrays.copyOfRange(args, 2, args.size)
        } else {
            Arrays.copyOfRange(args, 1, args.size)
        }

        if (!checkSenderType(commandSender, target) || !checkPermission(commandSender, target) || !checkArgsLength(
                commandSender,
                target,
                targetArgs
            )
        ) {
            // 校验失败时已发送明确提示，返回 true 抑制 Bukkit 再打一遍 usageMessage
            return true
        }

        return invokeCommand(commandExecutor, commandSender, label, target, targetArgs)
    }


    private fun sendCommandList(commandSender: CommandSender, label: String) {
        plugin?.sendMessage(commandSender, L(AXLanguageKey.COMMAND_LIST))
        subCommands.forEach { (key: String, value: AXCommandExecutor) ->
            plugin?.sendMessage(
                commandSender,
                L(AXLanguageKey.COMMAND_LIST_ITEM, label, key, value.description)
            )
        }
    }

    private fun getTargetFunction(
        commandExecutor: AXCommandExecutor,
        commandSender: CommandSender,
        label: String,
        args: Array<String>
    ): AXCommandFunction? {
        if (commandExecutor is SingleCommandExecutor) {
            return commandExecutor.function
        } else if (commandExecutor is MultiCommandExecutor) {
            if (args.size == 1) {
                sendSubCommandList(commandSender, label, args[0], commandExecutor)
                return null
            }
            return commandExecutor.getFunction(args[1])
        }
        return null
    }

    private fun sendSubCommandList(
        commandSender: CommandSender,
        label: String,
        cmd: String,
        multiCommandExecutor: MultiCommandExecutor
    ) {
        plugin?.sendMessage(commandSender, L(AXLanguageKey.SUB_COMMAND_LIST))
        multiCommandExecutor.functionMap.forEach { (key, value) ->
            val argWord: String = Arrays.stream(value.argsDescription)
                // ? 前缀表示可选参数：渲染为 [..]，必填渲染为 <..>，不把内部约定的 ? 暴露给玩家
                .map { argName -> if (argName.startsWith("?")) "&7[&6${argName.substring(1)}&7]" else "&7<&6$argName&7>" }
                .collect(Collectors.joining(" "))
            plugin?.sendMessage(
                commandSender,
                L(AXLanguageKey.SUB_COMMAND_LIST_ITEM, label, cmd, key, argWord, value.description)
            )
        }
    }

    private fun checkSenderType(commandSender: CommandSender, target: AXCommandFunction): Boolean {
        if (target.only === SenderType.ALL) return true
        // OP 为有意的硬门：与权限节点(checkPermission)相互独立，表达"仅完全管理权可用"，
        // 不走权限委派（即便授予了 permission，非 OP 也无法通过 OP 命令）。
        if (target.only === SenderType.OP && !commandSender.isOp) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.ONLY_OP))
            return false
        }
        if (target.only === SenderType.CONSOLE && commandSender !is ConsoleCommandSender) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.ONLY_CONSOLE))
            return false
        }
        if (target.only === SenderType.PLAYER && commandSender !is Player) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.ONLY_PLAYER))
            return false
        }
        return true
    }

    private fun checkPermission(commandSender: CommandSender, target: AXCommandFunction): Boolean {
        if (target.permission.isNotEmpty() && !commandSender.hasPermission(target.permission)) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.NO_PERMISSION, target.permission))
            return false
        }
        return true
    }

    private fun checkArgsLength(commandSender: CommandSender, target: AXCommandFunction, args: Array<String>): Boolean {
        if(target.argsDescription.isEmpty()){
            return true
        }
        if(target.argsDescription[target.argsDescription.size - 1].startsWith("?")){
            if (target.argsDescription.size - 1 > args.size ) {
                plugin?.sendMessage(commandSender, L(AXLanguageKey.ARGS_MISMATCH, target.argsDescription.contentToString()))
                return false
            }
            return true
        } else if (target.argsDescription.size > args.size) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.ARGS_MISMATCH, target.argsDescription.contentToString()))
            return false
        }
        return true
    }

    private fun invokeCommand(
        commandExecutor: AXCommandExecutor,
        commandSender: CommandSender,
        label: String,
        target: AXCommandFunction,
        args: Array<String>
    ): Boolean {
        val method = target.method
        try {
            method.invoke(commandExecutor, CommandContext(plugin!!, commandSender, target, args))
            return true
        } catch (e: IllegalAccessException) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.COMMAND_ERROR))
            plugin?.sendException(L(AXLanguageKey.EXCEPTION_COMMAND_INVOKE, label), e)
            // 已提示 COMMAND_ERROR，返回 true 抑制 Bukkit 再打 usageMessage
            return true
        } catch (e: InvocationTargetException) {
            plugin?.sendMessage(commandSender, L(AXLanguageKey.COMMAND_ERROR))
            plugin?.sendException(L(AXLanguageKey.EXCEPTION_COMMAND_INVOKE, label), e)
            return true
        }
    }
}