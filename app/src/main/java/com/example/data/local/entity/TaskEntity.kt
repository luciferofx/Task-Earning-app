package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.TaskCategory
import com.example.data.model.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val pointsReward: Int,
    val durationMinutes: Int,
    val status: TaskStatus = TaskStatus.AVAILABLE,
    val iconType: String = "default",
    val instructions: String,
    val proofRequirement: String,
    val submittedProofUrl: String? = null,
    val submittedNote: String? = null,
    val submittedTimestamp: Long? = null,
    val isFeatured: Boolean = false,
    val stepsListJson: String = ""
)
