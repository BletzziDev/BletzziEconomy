package com.bletzzi.economy.utils.database

import com.bletzzi.economy.utils.Console
import java.io.InputStream
import java.sql.DriverManager

data class Mysql(
    override val host: String,
    override val database: String,
    override val user: String,
    override val password: String,
    override val migrations: List<InputStream>
) : DataSource(host, database, user, password, migrations) {
    init {
        Console.log("§3TESTE: $ - $user - $database - $password")
        connection = DriverManager.getConnection("jdbc:mysql://$host/$database?autoReconnect=true&keepAlive=true&useSSL=false", user, password)
        performMigrations()
    }

    fun keepAlive() {
        connection!!.prepareStatement("SELECT 1;")
    }

    fun close() {
        connection!!.close()
    }
}