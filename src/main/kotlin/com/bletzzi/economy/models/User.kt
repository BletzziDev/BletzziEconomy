package com.bletzzi.economy.models

import java.util.UUID

data class User(
    val uuid: UUID,
    var balance: Double
)