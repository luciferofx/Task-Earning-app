package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.local.dao.TaskDao
import com.example.data.local.dao.TransactionDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserProfileEntity
import com.example.data.model.TaskCategory
import com.example.data.model.TaskStatus

class Converters {
    @TypeConverter
    fun fromCategory(value: TaskCategory): String = value.name

    @TypeConverter
    fun toCategory(value: String): TaskCategory = try {
        TaskCategory.valueOf(value)
    } catch (e: Exception) {
        TaskCategory.APP_DOWNLOAD
    }

    @TypeConverter
    fun fromStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toStatus(value: String): TaskStatus = try {
        TaskStatus.valueOf(value)
    } catch (e: Exception) {
        TaskStatus.AVAILABLE
    }
}

@Database(
    entities = [
        TaskEntity::class,
        TransactionEntity::class,
        UserProfileEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_earn_database_v2"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
