package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val userId: String = "user_default_101",
    val name: String = "Alex Rivera",
    val email: String = "alex.rivera@example.com",
    val avatarUrl: String = "",
    val balancePoints: Int = 4750,
    val totalEarnedPoints: Int = 12500,
    val totalWithdrawnPoints: Int = 7750,
    val streakDays: Int = 4,
    val lastCheckInTimestamp: Long = 0L,
    val referralCode: String = "EARN99X",
    val completedTasksCount: Int = 8,
    val isVpnDetected: Boolean = false,
    val isRootDetected: Boolean = false,
    val deviceFingerprint: String = "DEV-8392-XYZ",
    val isGoogleLinked: Boolean = true
)
