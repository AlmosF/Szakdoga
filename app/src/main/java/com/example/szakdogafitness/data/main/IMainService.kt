package com.example.szakdogafitness.data.main

import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IMainService{
    suspend fun getUser() : Flow<User?>
    suspend fun updateUser(user: User)
    suspend fun getTrainers(): Flow<List<User?>>
    //suspend fun getUsersDailyDatas(): Flow<DailyDatas?> //fixme("Workingone")
    suspend fun getUsersDailyDatas(): Flow<List<DailyData>>
    suspend fun applyForAppointment(trainer: String?, appointment: Map<String, List<Int>>)
    suspend fun fetchTrainersRequests(trainerId: String): Flow<Map<String, List<Int>>>
    suspend fun fetchRequestedAppointments(trainerId: String): Flow<Map<String, List<Int>>>
    //suspend fun generateNewDailyData(dailyDatas: DailyDatas) //fixme("Workingone")
    suspend fun generateNewDailyData()
}