package com.bletzzi.economy.utils.extensors

fun Double.isValid(): Boolean {
    return (!this.isNaN() && this.isFinite() && this >= 0)
}