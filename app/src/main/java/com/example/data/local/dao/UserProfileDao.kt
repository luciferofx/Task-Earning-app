package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Query("SELECT * FROM user_profiles")
    fun getAllUsers(): Flow<List<UserProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfileEntity)

    @Update
    suspend fun updateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profiles SET balancePoints = balancePoints + :pointsDelta, totalEarnedPoints = totalEarnedPoints + :earnedDelta, completedTasksCount = completedTasksCount + :completedDelta")
    suspend fun addPoints(pointsDelta: Int, earnedDelta: Int = pointsDelta, completedDelta: Int = 0)

    @Query("UPDATE user_profiles SET balancePoints = balancePoints - :pointsDeduction, totalWithdrawnPoints = totalWithdrawnPoints + :pointsDeduction")
    suspend fun deductPointsForPayout(pointsDeduction: Int)

    @Query("UPDATE user_profiles SET streakDays = :newStreak, lastCheckInTimestamp = :checkInTime, balancePoints = balancePoints + :bonusPoints, totalEarnedPoints = totalEarnedPoints + :bonusPoints")
    suspend fun updateDailyStreak(newStreak: Int, checkInTime: Long, bonusPoints: Int)

    @Query("UPDATE user_profiles SET isVpnDetected = :vpn, isRootDetected = :root")
    suspend fun updateSecurityFlags(vpn: Boolean, root: Boolean)

    @Query("DELETE FROM user_profiles")
    suspend fun clearProfile()
}
