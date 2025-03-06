package com.bletzzi.economy.models

import org.bukkit.inventory.ItemStack

data class MenuItem(
    val key: String,
    val slot: Int,
    val stack: ItemStack
)