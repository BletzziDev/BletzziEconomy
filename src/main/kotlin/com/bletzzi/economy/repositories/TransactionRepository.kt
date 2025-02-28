package com.bletzzi.economy.repositories

import com.bletzzi.economy.enums.TransactionStatus
import com.bletzzi.economy.models.Transaction
import com.bletzzi.economy.utils.database.DataSource
import com.bletzzi.economy.utils.database.Repository
import java.util.UUID

class TransactionRepository(private val source: DataSource) : Repository<UUID, Transaction>("transactions") {
    override fun internalSelect(key: UUID): Transaction? {
        val result = source.select(table, "*").where("`uuid` = $key").build().execute()
        if(!result.next()) return null
        return Transaction(
            key,
            UUID.fromString(result.getString("payer")),
            UUID.fromString(result.getString("payee")),
            result.getDouble("amount"),
            TransactionStatus.valueOf(result.getString("status"))
        )
    }

    override fun insert(key: UUID, value: Transaction) {
        source.insert(table, "uuid,payer,payee,amount,status", true).build()
            .bind(1, value.uuid.toString())
            .bind(2, value.payer.toString())
            .bind(3, value.payee.toString())
            .bind(4, value.amount)
            .bind(5, value.status.name)
            .execute()
    }

    override fun update(key: UUID, value: Transaction) {
        source.update(table, "payer,payee,amount,status").where("`uuid`=$key").build()
            .bind(1, value.payer.toString())
            .bind(2, value.payee.toString())
            .bind(3, value.amount)
            .bind(4, value.status.name)
            .execute()
    }

    override fun delete(key: UUID) {
        source.delete(table).where("`uuid`=$key").build().execute()
    }
}