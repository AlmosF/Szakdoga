package com.example.szakdogafitness.data.main

import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IMainService{
    suspend fun getUser() : Flow<User?>
    suspend fun getUsersDailyDatas(): Flow<DailyDatas?>
    suspend fun generateNewDailyData(dailyDatas: DailyDatas)
}