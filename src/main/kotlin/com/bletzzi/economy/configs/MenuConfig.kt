package com.bletzzi.economy.configs

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.models.Menu
import com.bletzzi.economy.models.MenuItem
import com.bletzzi.economy.utils.Console
import com.bletzzi.economy.utils.CustomStack
import com.bletzzi.economy.utils.DataFile
import com.bletzzi.economy.utils.extensors.color

class MenuConfig(plugin: EconomyPlugin) {
    val menus = hashMapOf<String, Menu>()

    init {
        val file = DataFile("menus.yml", plugin)

        file.getKeys(false).forEach {
            val items = hashMapOf<String, MenuItem>()
            file.getConfigurationSection("${it}.items").getKeys(false).forEach { item ->
                items[item] = MenuItem(
                    item,
                    file.getInt("${it}.items.${item}.slot"),
                    CustomStack.getByConfig(file, "${it}.items.${item}")
                )
            }

            menus[it] = Menu(
                it,
                file.getInt("${it}.size"),
                file.getString("${it}.title").color(),
                items
            )
        }

        Console.log("§aForam carregados §f${menus.size} §amenus.")
    }
}