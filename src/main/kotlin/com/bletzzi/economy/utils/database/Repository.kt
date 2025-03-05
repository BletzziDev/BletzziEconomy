package com.bletzzi.economy.utils.database

import java.util.*
import kotlin.collections.HashMap

abstract class Repository<K, V>(val table: String) {
    private val cache: HashMap<K, V> = hashMapOf()

    protected abstract fun internalSelect(key: K): V?
    abstract fun insert(key: K, value: V)
    abstract fun update(key: K, value: V)
    abstract fun updateAt(key: UUID, updates: Map<String, Any>)
    abstract fun delete(key: K)

    fun selectFromCache(key: K): V? {
        return cache[key]
    }

    fun select(key: K, useCache: Boolean): V? {
        if(useCache) {
            if(cache.containsKey(key)) return cache[key]
            val value = internalSelect(key)
            value?.let { cache[key] = it }
            return value
        }

        val value = internalSelect(key)
        value?.let { cache[key] = it }
        return value
    }

    fun deleteFromCache(key: K): V? {
        return cache.remove(key)
    }
}