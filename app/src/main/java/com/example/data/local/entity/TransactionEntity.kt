package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // "EARN", "PAYOUT", "BONUS", "REFERRAL"
    val points: Int,  // Positive for earn/bonus, negative for payout
    val amountFormatted: String, // e.g. "+500 pts ($0.50)" or "-5,000 pts ($5.00)"
    val status: String, // "COMPLETED", "PROCESSING", "PENDING", "REJECTED"
    val payoutMethod: String? = null,
    val destinationAccount: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
