package com.example.szakdogafitness.data.trainees

import android.util.Log
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TraineesService @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ITraineesService {
    override suspend fun getUsersDailyDatasById(userId: String): Flow<List<DailyData>> = flow {
        try {
            firestore.collection("dailyDatas")
                .document(userId)
                .collection("dailyData")
                .dataObjects<DailyData>()
                .collect { dailyDataList ->
                    emit(dailyDataList.sortedBy { it.date.toDate() })
                }
        } catch (e: Exception) {
            Log.e("MainService", "Error fetching daily data by ID: ${e.message}", e)
            emit(emptyList())
        }
    }

    override suspend fun deleteRequest(traineeId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun acceptRequest(traineeId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeTrainee(traineeId: String) {
        TODO("Not yet implemented")
    }

    override fun addEventToCalendar(
        event: Map<String, List<Int>>,
        title: String,
        description: String
    ) {
        TODO("Not yet implemented")
    }

    override fun removeEventFromCalendar(
        event: Map<String, List<Int>>,
        title: String,
        description: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRequests(status: String): Flow<List<Pair<Requests, User>>> {
        TODO("Not yet implemented")
    }

}