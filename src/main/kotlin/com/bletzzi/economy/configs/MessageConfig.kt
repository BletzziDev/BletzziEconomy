package com.bletzzi.economy.configs

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.utils.DataFile
import com.bletzzi.economy.utils.extensors.color

class MessageConfig(plugin: EconomyPlugin) {
    val string: HashMap<String, String> = hashMapOf()
    val list: HashMap<String, List<String>> = hashMapOf()

    init {
        val file = DataFile("messages.yml", plugin)

        string.clear()
        file.getConfigurationSection("string")!!.getKeys(false).forEach { string[it] = file.getString("string.$it")!!.color() }

        list.clear()
        file.getConfigurationSection("list")!!.getKeys(false).forEach { list[it] = file.getStringList("list.$it").color() }
    }
}