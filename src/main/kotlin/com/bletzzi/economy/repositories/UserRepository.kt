package com.bletzzi.economy.repositories

import com.bletzzi.economy.models.User
import com.bletzzi.economy.utils.database.DataSource
import com.bletzzi.economy.utils.database.Repository
import java.util.UUID

class UserRepository(private val dataSource: DataSource) : Repository<UUID, User>("bletzzieco_users") {

    override fun internalSelect(key: UUID): User {
        val result = dataSource.select(table, "*").where("`uuid` = $key").build().execute()
        if(!result.next()) return User(key, 0.0)
        return User(key, result.getDouble("balance"))
    }

    override fun insert(key: UUID, value: User) {
        dataSource.insert(table, "uuid, balance", true).build()
            .bind(1, key.toString())
            .bind(2, value.balance)
            .execute()
    }

    override fun update(key: UUID, value: User) {
        dataSource.update(table, "balance").where("`uuid` = ?").build()
            .bind(1, value.balance)
            .bind(2, key.toString())
            .execute()
    }

    override fun delete(key: UUID) {}
}