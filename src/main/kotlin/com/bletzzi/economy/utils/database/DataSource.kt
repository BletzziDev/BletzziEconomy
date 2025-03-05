package com.bletzzi.economy.utils.database

import com.bletzzi.economy.utils.Console
import java.io.InputStream
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class DataSource(
    open val host: String,
    open val database: String,
    open val user: String,
    open val password: String,
    open val migrations: List<InputStream>,
    var connection: Connection? = null
) {

    fun performMigrations() {
        Console.log("§2Iniciando migration")
        migrations.forEach {
            Console.log("§2Migration: $it")
            var migration = ""
            for(line in it.bufferedReader().readLines()) {
                migration += line
            }

            Console.log("§3Tentando estabelecer o statement")
            connection!!.prepareStatement(migration)?.let { stmt ->
                stmt.execute()
                stmt.close()
            }
        }
    }

    fun select(table: String, columns: String): QueryBuilder {
        return QueryBuilder("SELECT $columns FROM `$table`", this)
    }

    fun insert(table: String, columns: String, ignore: Boolean): QueryBuilder {
        var placeholders = ""
        val size = columns.replace(" ", "").split(",").size
        for(i in 1..size) placeholders += if(i == size) "?" else "?, "
        return QueryBuilder("INSERT${ if(ignore) " IGNORE" else "" } INTO $table ($columns) VALUES (${placeholders})", this)
    }

    fun update(table: String, columns: String): QueryBuilder {
        var placeholders = ""
        val data = columns.replace(" ", "").split(",")
        for(i in 1..data.size) placeholders += if(i == data.size) "`${data[i-1]}` = ? " else "`${data[i-1]}` = ?, "
        return QueryBuilder("UPDATE $table SET $placeholders", this)
    }

    fun delete(table: String): QueryBuilder {
        return QueryBuilder("DELETE FROM $table", this)
    }

    class QueryBuilder(private var sql: String, private val source: DataSource) {
        lateinit var stmt: PreparedStatement

        fun build(): QueryBuilder {
            stmt = source.connection!!.prepareStatement(sql)
            return this
        }

        fun execute(): PreparedStatement {
            sql += ";"
            Console.debug("§9SQL QUERY: $sql")
            stmt.execute()
            return stmt
        }

        fun where(condition: String): QueryBuilder {
            sql += " WHERE $condition"
            return this
        }

        fun orderBy(column: String): QueryBuilder {
            sql += " ORDER BY $column"
            return this
        }

        fun asc(): QueryBuilder {
            sql += " ASC"
            return this
        }

        fun desc(): QueryBuilder {
            sql += " DESC"
            return this
        }

        fun limit(limit: Int): QueryBuilder {
            sql += " LIMIT $limit"
            return this
        }

        fun bind(index: Int, value: String): QueryBuilder {
            stmt.setString(index, value)
            return this
        }
        fun bind(index: Int, value: Int): QueryBuilder {
            stmt.setInt(index, value)
            return this
        }
        fun bind(index: Int, value: Double): QueryBuilder {
            stmt.setDouble(index, value)
            return this
        }
        fun bind(index: Int, value: Boolean): QueryBuilder {
            stmt.setBoolean(index, value)
            return this
        }
        fun bindAny(index: Int, value: Any): QueryBuilder {
            when {
                value is Int -> bind(index, value)
                value is Double -> bind(index, value)
                value is Boolean -> bind(index, value)
                value is String -> bind(index, value)
                else -> try {
                    throw Exception("Invalid query bind value ($value)")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return this
        }
    }
}