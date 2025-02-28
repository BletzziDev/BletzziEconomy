package com.bletzzi.economy.commands

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.utils.NumberFormat
import me.saiintbrisson.minecraft.command.annotation.Command
import me.saiintbrisson.minecraft.command.annotation.Optional
import me.saiintbrisson.minecraft.command.command.Context
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoneyCommand(private val plugin: EconomyPlugin) {
    @Command(name = "money", aliases = ["coin", "coins", "dinheiro"] )
    fun root(context: Context<CommandSender>, @Optional target: Player?) {
        val sender = context.sender

        if(target != null) {
            sender.sendMessage(plugin.messageConfig.string["balanceOthers"]
                ?.replace("{player}", target.name)
                ?.replace("{balance}", NumberFormat.format(plugin.userRepository.select(target.uniqueId, true)!!.balance)))
            return
        }
    }
}