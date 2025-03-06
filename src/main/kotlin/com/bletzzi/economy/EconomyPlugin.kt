package com.bletzzi.economy

import com.bletzzi.economy.commands.MoneyCommand
import com.bletzzi.economy.configs.MainConfig
import com.bletzzi.economy.configs.MenuConfig
import com.bletzzi.economy.configs.MessageConfig
import com.bletzzi.economy.controllers.TransactionController
import com.bletzzi.economy.hooks.PapiExpansion
import com.bletzzi.economy.listeners.PlayerConnectionListener
import com.bletzzi.economy.repositories.TransactionRepository
import com.bletzzi.economy.repositories.UserRepository
import com.bletzzi.economy.utils.Console
import com.bletzzi.economy.utils.database.Mysql
import com.bletzzi.economy.vault.EconomyProvider
import com.bletzzi.economy.views.MainView
import me.devnatan.inventoryframework.ViewFrame
import me.saiintbrisson.bukkit.command.BukkitFrame
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class EconomyPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: EconomyPlugin
            private set
    }

    lateinit var mainConfig: MainConfig
    lateinit var messageConfig: MessageConfig
    lateinit var menuConfig: MenuConfig

    lateinit var mysql: Mysql
    lateinit var userRepository: UserRepository
    lateinit var transactionRepository: TransactionRepository

    lateinit var transactionController: TransactionController

    lateinit var bukkitFrame: BukkitFrame
    lateinit var viewFrame: ViewFrame

    override fun onEnable() {
        plugin = this

        loadPluginConfigs()

        if(!setupRepositories()) {
            Console.log("§cOcorreu um erro ao se conectar no banco de dados!")
            Console.log("§cVerifique se as informações estão corretas!")
            Console.log("§cVerifique se a conexão está disponível!")
            Console.log("§cO plugin irá ficar desligado enquanto o erro não for corrigido")
            pluginLoader.disablePlugin(this)
            return
        }

        transactionController = TransactionController(this)

        val vault = Bukkit.getPluginManager().getPlugin("Vault")
        if(vault == null || !vault.isEnabled) {
            Console.log("§cVocê não instalou o Vault API em seu servidor!")
            Console.log("§cFaça o download em §fhttps://www.spigotmc.org/resources/vault.34315/")
            Console.log("§cO plugin irá ficar desligado enquanto o vault não for instalado")
            pluginLoader.disablePlugin(this)
            return
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null) {
            Console.log("§aPlaceholderAPI encontrado, registrando expansão!")
            PapiExpansion(this).register()
        }

        loadListeners()

        Bukkit.getServicesManager().register(Economy::class.java, EconomyProvider(this), this, ServicePriority.Highest)

        bukkitFrame = BukkitFrame(this)
        bukkitFrame.registerCommands(MoneyCommand(this))

        viewFrame = ViewFrame.create(this)
        viewFrame.with(MainView(this)).register()

        Console.log("§aPlugin iniciado com sucesso!")
    }

    override fun onDisable() {
        // TODO Colocar pra salvar todos os dados dos players onlines quando servidor desligar
        Console.log("§cPlugin desligado com sucesso!")
    }

    fun loadPluginConfigs() {
        mainConfig = MainConfig(this)
        messageConfig = MessageConfig(this)
        menuConfig = MenuConfig(this)
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
        } catch(x: Exception) {
            x.printStackTrace()
            return false
        }
    }

    private fun loadListeners() {
        PlayerConnectionListener(this)
    }
}