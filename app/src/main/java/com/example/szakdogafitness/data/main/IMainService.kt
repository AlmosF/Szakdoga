package com.example.szakdogafitness.data.main

import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IMainService{
    suspend fun getUser() : Flow<User?>
    suspend fun updateUser(user: User)
    suspend fun getTrainers(): Flow<List<User?>>
    //suspend fun getUsersDailyDatas(): Flow<DailyDatas?> //fixme("Workingone")
    suspend fun getUsersDailyDatas(): Flow<List<DailyData>> //fixme("Workingone20250326")
    suspend fun getUsersDailyDatasById(userId: String): Flow<List<DailyData>>
    suspend fun applyForAppointment(trainer: String?, appointment: Map<String, List<Int>>)
    suspend fun fetchTrainersRequests(trainerId: String): Flow<Map<String, List<Int>>>
    suspend fun fetchRequestedAppointments(trainerId: String): Flow<Map<String, List<Int>>>
    suspend fun fetchRequestedAppoint(trainerId: String): Flow<Pair<Requests, User>>

    suspend fun deleteRequest(traineeId: String)
    suspend fun acceptRequest(traineeId: String)

    suspend fun getAcceptedTrainers(): Flow<List<Pair<Requests, User>>>

    //suspend fun fetchPendingRequests(status: String): Flow<List<Requests>>
    suspend fun fetchRequests(status: String): Flow<List<Pair<Requests, User>>>

    //suspend fun generateNewDailyData(dailyDatas: DailyDatas) //fixme("Workingone")
    suspend fun generateNewDailyData()

    suspend fun rateTrainer(trainerId: String, rate: Int)
    suspend fun loadTrainersRate(trainerId: String): Flow<Rating>
    suspend fun loadAllTrainersRates(): Flow<List<Pair<String, List<Rating>>>>

}