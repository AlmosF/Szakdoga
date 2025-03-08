package com.example.szakdogafitness.data.main

import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.Trainee
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IMain {
    suspend fun fetchDailyData(): Flow<DailyDatas>
    suspend fun fetchTrainers(): Flow<List<User>>
    suspend fun fetchTrainees(): Flow<List<User>>
    suspend fun addNewDailyData(
        current: DailyDatas, newDay: DailyData
    )
    suspend fun editDailyData(dailyData: DailyData) : Flow<DailyDatas>
}