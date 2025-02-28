package com.bletzzi.economy.api.events

import com.bletzzi.economy.models.Transaction
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CreateTransactionEvent(
    val transaction: Transaction,
    var isCancelled: Boolean = false
) : Event() {
    companion object {
        val handlers: HandlerList = HandlerList()
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return CreateTransactionEvent.handlers
    }
}