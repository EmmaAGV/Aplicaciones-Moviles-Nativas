package com.example.routinepro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val muscleGroup: String,
    val description: String,
    val videoUrl: String? = null,
    val isCustom: Boolean = false
)

@Entity(tableName = "workout_history")
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseName: String,
    val muscleGroup: String,
    val sets: Int,
    val reps: Int,
    val weight: Float,
    val date: Long = System.currentTimeMillis(),
    val durationSeconds: Int = 0
)

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val glasses: Int
)

@Entity(tableName = "body_measurements")
data class BodyMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weight: Float,
    val neck: Float = 0f,
    val chest: Float = 0f,
    val waist: Float = 0f,
    val hip: Float = 0f,
    val bicep: Float = 0f,
    val date: Long = System.currentTimeMillis()
)

@Entity(tableName = "wellness_tips")
data class WellnessTip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // Nutrición, Recuperación, Actitud
    val title: String,
    val content: String,
    val imageUrl: String? = null
)
