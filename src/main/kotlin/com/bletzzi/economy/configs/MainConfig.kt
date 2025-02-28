package com.bletzzi.economy.configs

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.utils.DataFile
import org.bukkit.configuration.ConfigurationSection

class MainConfig(plugin: EconomyPlugin) {
    val mysqlCredentials: ConfigurationSection

    init {
        val file = DataFile("config.yml", plugin)
        mysqlCredentials = file.getConfigurationSection("mysql")!!
    }
}