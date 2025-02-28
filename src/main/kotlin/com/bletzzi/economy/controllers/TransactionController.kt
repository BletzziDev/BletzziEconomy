package com.bletzzi.economy.controllers

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.enums.TransactionStatus
import com.bletzzi.economy.models.Transaction
import org.bukkit.Bukkit
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class TransactionController(private val plugin: EconomyPlugin) {
    companion object {
        val serverId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

    /*
    *   CRIAR UMA LISTA COM TRANSAÇÕES QUE CASO O JOGADOR ESTEJA ONLINE IRÁ ACEITAR A TRANSAÇÃO E SALVAR NA DB
    *   CASO O JOGADOR ESTEJA OFF IRÁ CRIAR UMA TRANSAÇÃO NO REPOSITÓRIO E REMOVER DA FILA PARA ASSIM
    *   EM OUTRO SERVIDOR TENTAR SETAR
    *
    *   A CADA 20 TICKS O SERVIDOR IRÁ FAZER UMA SOLICITAÇÃO PEGANDO TODAS AS TRANSAÇÕES PENDENTES NO
    *   BANCO DE DADOS E TENTAR APROVAR-LAS
    * */

    val serverTransactions: ConcurrentHashMap<UUID, Double> = ConcurrentHashMap()
    val transactionQueue: List<Transaction> = arrayListOf()

    init {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, {

        }, 5L, 5L)
    }

    fun attemptTransactionUpdate(transaction: Transaction): Boolean {
        Bukkit.getPlayer(transaction.payee)?.let {
            plugin.userRepository.select(transaction.payee, true)?.let {
                it.balance += transaction.amount
                transaction.status = TransactionStatus.SUCCESS
                plugin.transactionRepository.update(transaction.uuid, transaction)
                plugin.userRepository.update(transaction.payee, it)
                return true
            }
        }
        return false
    }
}