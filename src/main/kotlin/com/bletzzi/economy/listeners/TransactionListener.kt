package com.bletzzi.economy.listeners

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.api.events.CreateTransactionEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class TransactionListener(val plugin: EconomyPlugin) : Listener {
    init { Bukkit.getPluginManager().registerEvents(this, plugin) }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onTransaction(event: CreateTransactionEvent) {
        if(event.isCancelled) return

        val transaction = event.transaction
    }
}