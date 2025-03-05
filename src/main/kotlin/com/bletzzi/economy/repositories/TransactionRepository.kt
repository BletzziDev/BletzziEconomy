package com.bletzzi.economy.repositories

import com.bletzzi.economy.enums.TransactionStatus
import com.bletzzi.economy.models.Transaction
import com.bletzzi.economy.utils.database.DataSource
import com.bletzzi.economy.utils.database.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class TransactionRepository(private val source: DataSource) : Repository<UUID, Transaction>("bletzzieco_transactions") {
    fun selectAllPending(): ArrayList<Transaction> {
        val transactions = arrayListOf<Transaction>()
        val query = source.select(table, "*").where("`status` = 'PENDING'").build().execute()
        val result = query.resultSet
        while(result.next()) {
            transactions.add(Transaction(
                UUID.fromString(result.getString("uuid")),
                UUID.fromString(result.getString("payer")),
                UUID.fromString(result.getString("payee")),
                result.getDouble("amount"),
                TransactionStatus.valueOf(result.getString("status"))
            ))
        }
        query.close()
        result.close()

        return transactions
    }

    override fun internalSelect(key: UUID): Transaction? {
        val query = source.select(table, "*").where("`uuid` = '$key'").build().execute()
        val result = query.resultSet
        if(!result.next()) return null
        val transaction = Transaction(
            key,
            UUID.fromString(result.getString("payer")),
            UUID.fromString(result.getString("payee")),
            result.getDouble("amount"),
            TransactionStatus.valueOf(result.getString("status"))
        )
        query.close()
        result.close()
        return transaction
    }

    override fun insert(key: UUID, value: Transaction) {
        CoroutineScope(Dispatchers.IO).launch {
            source.insert(table, "uuid,payer,payee,amount,status", true).build()
                .bind(1, value.uuid.toString())
                .bind(2, value.payer.toString())
                .bind(3, value.payee.toString())
                .bind(4, value.amount)
                .bind(5, value.status.name)
                .execute().close()
        }
    }

    override fun update(key: UUID, value: Transaction) {
        CoroutineScope(Dispatchers.IO).launch {
            source.update(table, "payer,payee,amount,status").where("`uuid`='$key'").build()
                .bind(1, value.payer.toString())
                .bind(2, value.payee.toString())
                .bind(3, value.amount)
                .bind(4, value.status.name)
                .execute().close()
        }
    }

    override fun delete(key: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            source.delete(table).where("`uuid`='$key'").build().execute().close()
        }
    }

    override fun updateAt(key: UUID, updates: Map<String, Any>) {}
}