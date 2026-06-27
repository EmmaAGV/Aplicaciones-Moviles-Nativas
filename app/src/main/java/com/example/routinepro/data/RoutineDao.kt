package com.example.routinepro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoutineDao {
    // Exercises
    @Insert
    suspend fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<Exercise>

    // History
    @Insert
    suspend fun insertHistory(entry: HistoryEntry)

    @Query("SELECT * FROM workout_history ORDER BY date DESC")
    suspend fun getFullHistory(): List<HistoryEntry>

    @Query("SELECT SUM(sets * reps * weight) FROM workout_history WHERE date >= :since")
    suspend fun getTotalVolume(since: Long): Float?

    // Water
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterLog(log: WaterLog)

    @Query("SELECT SUM(glasses) FROM water_logs WHERE date >= :startOfDay")
    suspend fun getTodayWater(startOfDay: Long): Int?

    // Measurements
    @Insert
    suspend fun insertMeasurement(measurement: BodyMeasurement)

    @Query("SELECT * FROM body_measurements ORDER BY date DESC")
    suspend fun getAllMeasurements(): List<BodyMeasurement>

    // Wellness
    @Query("SELECT * FROM wellness_tips")
    suspend fun getWellnessTips(): List<WellnessTip>
    
    @Insert
    suspend fun insertWellnessTip(tip: WellnessTip)
}
