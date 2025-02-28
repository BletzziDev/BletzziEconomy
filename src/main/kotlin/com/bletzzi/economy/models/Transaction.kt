package com.bletzzi.economy.models

import com.bletzzi.economy.enums.TransactionStatus
import java.util.UUID

data class Transaction(
    val uuid: UUID,
    val payer: UUID,
    val payee: UUID,
    val amount: Double,
    var status: TransactionStatus
)