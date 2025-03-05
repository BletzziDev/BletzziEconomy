package com.bletzzi.economy.models

import org.bukkit.Bukkit
import java.util.UUID

data class User(
    val uuid: UUID,
    var balance: Double
) {
    fun isOnline(): Boolean {
        val player = Bukkit.getPlayer(uuid)
        return !(player == null || !player.isOnline)
    }
}