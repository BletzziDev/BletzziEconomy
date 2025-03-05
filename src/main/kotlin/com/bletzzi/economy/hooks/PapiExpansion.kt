package com.bletzzi.economy.hooks

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.utils.NumberFormat
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PapiExpansion(val plugin: EconomyPlugin) : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "beconomy"
    }

    override fun getAuthor(): String {
        return "bletzzi.com"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun onPlaceholderRequest(player: Player?, params: String): String {
        if(player == null) return "-/-"

        val user = plugin.userRepository.select(player.uniqueId, true)!!

        if(params.equals("balance", true)) {
            return NumberFormat.format(user.balance)
        }

        if(params.equals("balance_raw", true)) {
            return user.balance.toString()
        }

        return "-/-"
    }
}