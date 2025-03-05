package com.bletzzi.economy.repositories

import com.bletzzi.economy.models.User
import com.bletzzi.economy.utils.Console
import com.bletzzi.economy.utils.database.DataSource
import com.bletzzi.economy.utils.database.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class UserRepository(private val dataSource: DataSource) : Repository<UUID, User>("bletzzieco_users") {

    override fun internalSelect(key: UUID): User {
        val query = dataSource.select(table, "*").where("`uuid` = '$key'").build().execute()
        val result = query.resultSet
        if(!result.next()) {
            Console.log("§cDebug user repository não tem user na db")
            val user = User(key, 0.0)
            insert(user.uuid, user)
            return user
        }
        val user = User(key, result.getDouble("balance"))
        query.close()
        result.close()
        return user
    }

    override fun insert(key: UUID, value: User) {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.insert(table, "uuid, balance", true).build()
                .bind(1, key.toString())
                .bind(2, value.balance)
                .execute().close()
        }
    }

    override fun update(key: UUID, value: User) {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.update(table, "balance").where("`uuid` = ?").build()
                .bind(1, value.balance)
                .bind(2, key.toString())
                .execute().close()
        }
    }

    override fun updateAt(key: UUID, updates: Map<String, Any>) {
        CoroutineScope(Dispatchers.IO).launch {
            val columns = updates.keys.joinToString(",")
            val query = dataSource.update(table, columns).where("`uuid`='$key'").build()

            var index = 1
            for(value in updates.values) {
                query.bindAny(index, value)
                index++
            }

            query.execute().close()
        }
    }

    override fun delete(key: UUID) {}
}