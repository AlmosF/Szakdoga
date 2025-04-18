package com.example.szakdogafitness.data.dailydatas

import android.util.Log
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class DailyDatasService @Inject constructor(
    private val userIdUseCase: CurrentUserIdUseCase,
    private val firestore: FirebaseFirestore,
): IDailyDatasService {
    override suspend fun getUsersDailyDatas(): Flow<List<DailyData>> = flow {
        val userId = userIdUseCase()
        Log.d("MainService", "Fetching daily data for userId: $userId")
        try {
            firestore.collection("dailyDatas")
                .document(userId!!)
                .collection("dailyData")
                .dataObjects<DailyData>()
                .catch { e ->
                    Log.e("MainService", "Error fetching daily data: ${e.message}", e)
                    emit(emptyList())
                }
                .collect { dailyDataList ->
                    //val sortedDailyDataList = dailyDataList.sortedBy { it.date.toDate() }
                    //emit(sortedDailyDataList)
                    //Log.d("MainService", "Fetched real-time daily data: $sortedDailyDataList")
                    emit(dailyDataList.sortedBy { it.date.toDate() })
                }
        } catch (e: Exception) {
            Log.e("MainService", "Error fetching real-time daily data: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun modifyTargetKcal(
        targetKcal: Int,
        date: Timestamp
    ) {
        val userId = userIdUseCase()
        val dateFormated = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.toDate())
        try {
            val dailyDataRef = firestore.collection("dailyDatas")
                .document(userId!!)
                .collection("dailyData")
                .document(dateFormated)
            firestore.runTransaction { transaction ->
                transaction.update(dailyDataRef, "targetKcal", targetKcal)
            }
        } catch (e: Exception) {
            Log.e("MainService", "Error modifying target kcal: ${e.message}", e)
        }
    }

    override suspend fun modifyTargetWater(
        targetWater: Double,
        date: Timestamp
    ) {
        val userId = userIdUseCase()
        val dateFormated = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.toDate())
        try {
            val dailyDataRef = firestore.collection("dailyDatas")
                .document(userId!!)
                .collection("dailyData")
                .document(dateFormated)
            firestore.runTransaction { transaction ->
                transaction.update(dailyDataRef, "targetWater", targetWater)
            }
        } catch (e: Exception) {
            Log.e("MainService", "Error modifying target water: ${e.message}", e)
        }
    }

    override suspend fun updateWaterIntake(
        waterIntake: Double,
        date: Timestamp
    ) {
        val userId = userIdUseCase()
        val dateFormated = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.toDate())
        try {
            val dailyDataRef = firestore.collection("dailyDatas")
                .document(userId!!)
                .collection("dailyData")
                .document(dateFormated)
            firestore.runTransaction { transaction ->
                transaction.update(dailyDataRef, "waterIntake", waterIntake)
            }
        } catch (e: Exception) {
            Log.e("MainService", "Error updating water intake: ${e.message}", e)
        }
    }

}