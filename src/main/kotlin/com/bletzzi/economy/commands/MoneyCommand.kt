package com.bletzzi.economy.commands

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.enums.TransactionStatus
import com.bletzzi.economy.utils.NumberFormat
import com.bletzzi.economy.utils.extensors.isValid
import com.bletzzi.economy.views.MainView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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

        if(sender !is Player) {
            sender.sendMessage(plugin.messageConfig.string["onlyPlayers"])
            return
        }

        //val user = plugin.userRepository.select(sender.uniqueId, true)
        plugin.viewFrame.open(MainView::class.java, context.sender as Player)
        //sender.sendMessage(plugin.messageConfig.string["balanceSelf"]?.replace("{balance}", NumberFormat.format(user!!.balance)))
    }

    @Command(name = "money.send", aliases = ["money.enviar", "money.pay", "money.pagar"], permission = "beconomy.send")
    fun send(context: Context<CommandSender>, target: Player, amount: Double) {
        if(context.sender !is Player) {
            context.sendMessage(plugin.messageConfig.string["onlyPlayers"])
            return
        }

        if(!amount.isValid()) {
            context.sendMessage(plugin.messageConfig.string["invalidNumber"])
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val status = async { plugin.transactionController.performSendTransaction((context.sender as Player).uniqueId, target.uniqueId, amount) }.await()
            when (status) {
                TransactionStatus.PENDING -> context.sendMessage(plugin.messageConfig.string["successSend"]
                    ?.replace("{amount}", NumberFormat.format(amount))
                    ?.replace("{player}", target.name))
                else -> context.sendMessage(plugin.messageConfig.string["noFunds"])
            }
        }
    }

    @Command(name = "money.set", permission = "beconomy.set")
    fun set(context: Context<CommandSender>, target: Player, amount: Double, @Optional silent: Boolean?) {
        plugin.transactionController.performSetTransaction(target.uniqueId, amount)
        if(silent == null || !silent) context.sendMessage(plugin.messageConfig.string["successSet"]
            ?.replace("{amount}", NumberFormat.format(amount))
            ?.replace("{player}", target.name))
    }

    @Command(name = "money.add", permission = "beconomy.add")
    fun add(context: Context<CommandSender>, target: Player, amount: Double, @Optional silent: Boolean?) {
        plugin.transactionController.performAddTransaction(target.uniqueId, amount)
        if(silent == null || !silent) context.sendMessage(plugin.messageConfig.string["successAdd"]
            ?.replace("{amount}", NumberFormat.format(amount))
            ?.replace("{player}", target.name))
    }

    @Command(name = "money.remove", permission = "beconomy.remove")
    fun remove(context: Context<CommandSender>, target: Player, amount: Double, @Optional silent: Boolean?) {
        plugin.transactionController.performRemoveTransaction(target.uniqueId, amount)
        if(silent == null || !silent) context.sendMessage(plugin.messageConfig.string["successRemove"]
            ?.replace("{amount}", NumberFormat.format(amount))
            ?.replace("{player}", target.name))
    }
}