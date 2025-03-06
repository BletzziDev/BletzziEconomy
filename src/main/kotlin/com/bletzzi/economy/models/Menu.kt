package com.bletzzi.economy.models

data class Menu(
    val key: String,
    val size: Int,
    val title: String,
    val items: HashMap<String, MenuItem>
)