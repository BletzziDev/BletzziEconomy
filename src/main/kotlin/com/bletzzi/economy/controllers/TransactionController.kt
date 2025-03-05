package com.bletzzi.economy.controllers

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.enums.TransactionStatus
import com.bletzzi.economy.models.Transaction
import com.bletzzi.economy.models.User
import com.bletzzi.economy.utils.NumberFormat
import com.bletzzi.economy.utils.extensors.isValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class TransactionController(private val plugin: EconomyPlugin) {
    companion object {
        val serverId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

    private val serverTransactions = ConcurrentHashMap<UUID, Double>()

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, {
            val affectedUsers: ArrayList<User> = arrayListOf()

            try {
                serverTransactions.forEach { uuid, value ->
                    val user = plugin.userRepository.select(uuid, true)
                    if(user == null || !user.isOnline()) {
                        val transaction = Transaction(UUID.randomUUID(), serverId, uuid, value, TransactionStatus.PENDING)
                        plugin.transactionRepository.insert(transaction.uuid, transaction)
                        serverTransactions.remove(uuid)
                        return@forEach
                    }
                    user.balance += value
                    if(!affectedUsers.contains(user)) affectedUsers.add(user)
                    serverTransactions.remove(uuid)
                }
            } catch(x: Exception) {
               x.printStackTrace()
            }

            affectedUsers.forEach { try { plugin.userRepository.update(it.uuid, it)} catch(x: Exception) {x.printStackTrace()} }
        }, 5L, 5L)

        Bukkit.getScheduler().runTaskTimer(plugin, {
            CoroutineScope(Dispatchers.IO).launch {
                val transactions = async { plugin.transactionRepository.selectAllPending() }.await()
                val affectedUsers: ArrayList<User> = arrayListOf()
                transactions.forEach {
                    try {
                        val payeeUser = plugin.userRepository.select(it.payee, true)
                        if(payeeUser == null || !payeeUser.isOnline()) return@forEach

                        payeeUser.balance += it.amount
                        it.status = TransactionStatus.SUCCESS

                        Bukkit.getPlayer(payeeUser.uuid)!!.sendMessage(plugin.messageConfig.string["successReceived"]
                            ?.replace("{amount}", NumberFormat.format(it.amount))
                            ?.replace("{player}", Bukkit.getOfflinePlayer(it.payer).name)
                        )

                        if(!affectedUsers.contains(payeeUser)) affectedUsers.add(payeeUser)
                        plugin.transactionRepository.update(it.uuid, it)
                    } catch(x: Exception) {
                        x.printStackTrace()
                    }
                }

                affectedUsers.forEach { try { plugin.userRepository.update(it.uuid, it)} catch(x: Exception) {x.printStackTrace()} }
            }
        }, 20L, 20L)
    }

    fun performSetTransaction(uuid: UUID, amount: Double) {
        if(!amount.isValid()) return

        val user = plugin.userRepository.select(uuid, true)
        user?.balance = amount

        plugin.userRepository.updateAt(uuid, mapOf("balance" to ( if(amount >= 0) amount else 0 )))
    }

    fun performAddTransaction(uuid: UUID, amount: Double) {
        if(!amount.isValid()) return
        serverTransactions[uuid] = serverTransactions.getOrDefault(uuid, 0.0) + amount
    }

    fun performRemoveTransaction(uuid: UUID, amount: Double) {
        if(!amount.isValid()) return
        serverTransactions[uuid] = serverTransactions.getOrDefault(uuid, 0.0) - amount
    }

    fun performSendTransaction(payerUuid: UUID, payeeUuid: UUID, amount: Double): TransactionStatus {
        if(!amount.isValid()) return TransactionStatus.CANCELLED
        plugin.userRepository.select(payerUuid, false)?.let {
            if(amount > it.balance) return TransactionStatus.CANCELLED

            val payer = plugin.userRepository.select(payerUuid, true)!!
            payer.balance -= amount
            plugin.userRepository.update(payerUuid, payer)

            Transaction(UUID.randomUUID(), payerUuid, payeeUuid, amount, TransactionStatus.PENDING).let { transaction ->
                plugin.transactionRepository.insert(transaction.uuid, transaction)
            }
        }

        return TransactionStatus.PENDING
    }
}