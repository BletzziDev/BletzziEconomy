package com.bletzzi.economy

import com.bletzzi.economy.configs.MainConfig
import com.bletzzi.economy.configs.MessageConfig
import com.bletzzi.economy.listeners.PlayerConnectionListener
import com.bletzzi.economy.repositories.TransactionRepository
import com.bletzzi.economy.repositories.UserRepository
import com.bletzzi.economy.utils.Console
import com.bletzzi.economy.utils.database.Mysql
import org.bukkit.plugin.java.JavaPlugin

class EconomyPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: EconomyPlugin
            private set
    }

    lateinit var mainConfig: MainConfig
    lateinit var messageConfig: MessageConfig

    lateinit var mysql: Mysql
    lateinit var userRepository: UserRepository
    lateinit var transactionRepository: TransactionRepository

    override fun onEnable() {
        plugin = this

        loadPluginConfigs()

        if(!setupRepositories()) {
            Console.log("§cOcorreu um erro ao se conectar no banco de dados!")
            Console.log("§cVerifique se as informações estão corretas!")
            Console.log("§cVerifique se a conexão está disponível!")
            Console.log("§cO plugin será desligado enquanto o erro não for corrigido")
            pluginLoader.disablePlugin(this)
            return
        }

        Console.log("§aPlugin iniciado com sucesso!")
    }

    override fun onDisable() {
        Console.log("§cPlugin desligado com sucesso!")
    }

    fun loadPluginConfigs() {
        mainConfig = MainConfig(this)
        messageConfig = MessageConfig(this)
    }

    private fun setupRepositories(): Boolean {
        try {
            val credentials = mainConfig.mysqlCredentials
            mysql = Mysql(
                credentials.getString("host")!!,
                credentials.getString("database")!!,
                credentials.getString("user")!!,
                credentials.getString("password")!!,
                listOf(
                    getResource("V1__create_user_table.sql")!!,
                    getResource("V2__create_transactions_table.sql")!!
                )
            )

            userRepository = UserRepository(mysql)
            transactionRepository = TransactionRepository(mysql)
            return true
        } catch(x: Exception) { return false }
    }

    private fun loadListeners() {
        PlayerConnectionListener(this)
    }
}