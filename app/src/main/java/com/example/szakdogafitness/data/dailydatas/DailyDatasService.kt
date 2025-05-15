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
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DailyDatasService @Inject constructor(
    private val userIdUseCase: CurrentUserIdUseCase,
    private val firestore: FirebaseFirestore,
): IDailyDatasService {
    override suspend fun getUsersDailyDatas(): Flow<List<DailyData>> = flow {
        val userId = userIdUseCase()
        try {
            firestore.collection("dailyDatas")
                .document(userId!!)
                .collection("dailyData")
                .dataObjects<DailyData>()
                .catch { e ->
                    emit(emptyList())
                }
                .collect { dailyDataList ->
                    emit(dailyDataList.sortedBy { it.date.toDate() })
                }
        } catch (e: Exception) {
            Log.e("MainService", "Error fetching real-time daily data: ${e.message}", e)
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun generateNewDailyData() {
        val userId = userIdUseCase()

        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        try {
            val userDocRef = firestore.collection("dailyDatas").document(userId!!)
            val dailyDataCollectionRef = userDocRef.collection("dailyData")

            val existingDoc = dailyDataCollectionRef.document(todayDate).get().await()

            if (!existingDoc.exists()) {
                val newDailyData = DailyData(date = Timestamp.now())

                dailyDataCollectionRef
                    .document(todayDate)
                    .set(newDailyData)
                    .await()

                Log.d("MainService", "Created new daily data for user: $userId")
            } else {
                Log.d("MainService", "Today's daily data already exists for user: $userId")
            }

        } catch (e: Exception) {
            Log.e("MainService", "Error generating new daily data: ${e.message}", e)
        }
    }

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