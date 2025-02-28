package com.bletzzi.economy.utils.extensors

import com.bletzzi.economy.utils.Text

fun String.color(): String {
    return this.replace("&", "§")
}
fun List<String>.color(): List<String> {
    return Text.listReplace(this, "&", "§")
}