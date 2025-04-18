package com.example.szakdogafitness.data.trainers

import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ITrainersService {

    suspend fun getTrainers(): Flow<List<User?>>
    suspend fun applyForAppointment(trainer: String?, appointment: Map<String, List<Int>>)
    suspend fun fetchTrainersRequests(trainerId: String): Flow<Map<String, List<Int>>>
    suspend fun fetchRequestedAppoint(trainerId: String): Flow<Pair<Requests, User>>
    suspend fun getAcceptedTrainers(): Flow<List<Pair<Requests, User>>>
    suspend fun rateTrainer(trainerId: String, rate: Int)
    suspend fun loadTrainersRate(trainerId: String): Flow<Rating>
    suspend fun loadAllTrainersRates(): Flow<List<Pair<String, List<Rating>>>>

}