package com.example.szakdogafitness.data.dailydatas

import com.example.szakdogafitness.domain.model.DailyData
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow

interface IDailyDatasService {

    suspend fun getUsersDailyDatas(): Flow<List<DailyData>>
    suspend fun generateNewDailyData()
    suspend fun modifyTargetKcal(targetKcal: Int, date: Timestamp = Timestamp.now())
    suspend fun modifyTargetWater(targetWater: Double, date: Timestamp = Timestamp.now())
    suspend fun updateWaterIntake(waterIntake: Double, date: Timestamp = Timestamp.now())

}