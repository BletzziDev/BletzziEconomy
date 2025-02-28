package com.bletzzi.economy.utils

import org.bukkit.Bukkit

object Console {
    val prefix = "§6[BletzziEconomy]"
    val debugMode = false

    fun log(message: String) {
        Bukkit.getConsoleSender().sendMessage("$prefix §f${Text.colorTranslate(message)}")
    }

    fun debug(message: String) {
        if(!debugMode) return
        Bukkit.getConsoleSender().sendMessage("$prefix §c[DEBUG] §f${Text.colorTranslate(message)}")
    }
}