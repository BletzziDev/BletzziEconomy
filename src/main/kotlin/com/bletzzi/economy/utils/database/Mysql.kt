package com.bletzzi.economy.utils.database

import java.io.InputStream
import java.sql.DriverManager

data class Mysql(
    override val host: String,
    override val database: String,
    override val user: String,
    override val password: String,
    val migrations: List<InputStream>
) : DataSource(host, database, user, password, migrations) {
    init {
        connection = DriverManager.getConnection("jdbc:mysql://$host/$database?keepAlive=true&useSSL=false", user, password)
    }

    fun keepAlive() {
        connection!!.prepareStatement("SELECT 1;")
    }

    fun close() {
        connection!!.close()
    }
}