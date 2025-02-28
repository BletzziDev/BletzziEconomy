package com.bletzzi.economy.utils.database

import java.io.InputStream
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class DataSource(
    open val host: String,
    open val database: String,
    open val user: String,
    open val password: String,
    migrations: List<InputStream>,
    var connection: Connection? = null
) {
    init {
        migrations.forEach {
            var migration = ""
            for(line in it.bufferedReader().readLines()) {
                migration += line
            }

            connection!!.prepareStatement(migration).execute()
        }
    }

    fun select(table: String, columns: String): QueryBuilder {
        return QueryBuilder("SELECT $columns FROM `$table`", this)
    }

    fun insert(table: String, columns: String, ignore: Boolean): QueryBuilder {
        var placeholders = ""
        for(i in 1..(columns.replace(" ", "").split(",").size)) placeholders += "? "
        return QueryBuilder("INSERT${ if(ignore) " IGNORE" else "" } INTO $table ($columns) VALUES ( ${placeholders} )", this)
    }

    fun update(table: String, columns: String): QueryBuilder {
        var placeholders = ""
        for(column in columns.replace(" ", "").split(",")) placeholders += "`$column` = ? "
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

        fun execute(): ResultSet {
            sql += ";"
            stmt.execute()
            return stmt.resultSet
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
    }
}