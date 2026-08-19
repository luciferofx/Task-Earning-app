package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val userId: String = "user_default_101",
    val name: String = "Rahul Sharma",
    val email: String = "rahul.sharma@gmail.com",
    val phoneNumber: String = "+91 98765 43210",
    val avatarUrl: String = "",
    val balancePoints: Int = 4850,
    val totalEarnedPoints: Int = 14500,
    val totalWithdrawnPoints: Int = 9650,
    val streakDays: Int = 4,
    val lastCheckInTimestamp: Long = 0L,
    val referralCode: String = "INDIA99X",
    val completedTasksCount: Int = 8,
    val isVpnDetected: Boolean = false,
    val isRootDetected: Boolean = false,
    val deviceFingerprint: String = "IN-DEV-8392-XYZ",
    val isGoogleLinked: Boolean = true,
    val role: String = "USER", // "USER" or "ADMIN"
    val upiVpa: String = "rahul.sharma@paytm"
)
