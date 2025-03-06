package com.bletzzi.economy.utils.extensors

fun List<String>.replace(old: String, new: String): ArrayList<String> {
    val list = arrayListOf<String>()
    for(i in 1..this.size) {
        list.add(this[i-1].replace(old, new))
    }
    return list
}