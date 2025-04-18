package com.example.szakdogafitness.data.trainees

import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ITraineesService {

    suspend fun getUsersDailyDatasById(userId: String): Flow<List<DailyData>>
    suspend fun deleteRequest(traineeId: String)
    suspend fun acceptRequest(traineeId: String)
    suspend fun removeTrainee(traineeId: String)
    fun addEventToCalendar(event: Map<String, List<Int>>, title: String, description: String)
    fun removeEventFromCalendar(event: Map<String, List<Int>>, title: String, description: String)
    suspend fun fetchRequests(status: String): Flow<List<Pair<Requests, User>>>


}