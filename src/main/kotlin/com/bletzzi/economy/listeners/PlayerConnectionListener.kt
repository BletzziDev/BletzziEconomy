package com.bletzzi.economy.listeners

import com.bletzzi.economy.EconomyPlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerConnectionListener(private val plugin: EconomyPlugin) : Listener {
    init { Bukkit.getPluginManager().registerEvents(this, plugin) }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.userRepository.select(player.uniqueId, false)
    }

    /*
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        plugin.userRepository.deleteFromCache(player.uniqueId)?.let { plugin.userRepository.update(player.uniqueId, it) }
    }
     */
}