package com.bletzzi.economy.configs

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.utils.Console
import com.bletzzi.economy.utils.DataFile
import com.bletzzi.economy.utils.NumberFormat
import org.bukkit.configuration.ConfigurationSection

class MainConfig(plugin: EconomyPlugin) {
    val mysqlCredentials: ConfigurationSection
    val numberFormatSettings: ConfigurationSection

    init {
        val file = DataFile("config.yml", plugin)
        mysqlCredentials = file.getConfigurationSection("mysql")!!
        Console.log("§4MYSQL CREDENTIALS: $mysqlCredentials")
        numberFormatSettings = file.getConfigurationSection("format")!!

        NumberFormat.setup(numberFormatSettings)
    }
}