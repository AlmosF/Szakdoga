package com.example.szakdogafitness.data.main

import android.util.Log
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.FirestoreTrainee
import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.Trainee
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.model.toUser
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase
) : IMainService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUser(): Flow<User?> =
        firestore.collection("users")
            .whereEqualTo("uid", userIdUseCase())
            .limit(1)
            .dataObjects<FirestoreUser>()
            .flatMapLatest { userList->
                val user = userList.firstOrNull() ?: return@flatMapLatest flowOf(null)
                if (user.role == 1) {
                    firestore.collection("trainers")
                        .whereEqualTo("uid", userIdUseCase())
                        .limit(1)
                        .dataObjects<FirestoreTrainer>()
                        .map { trainers ->
                            val trainer = trainers.firstOrNull() ?: FirestoreTrainer()
                            user.toUser(trainer)
                        }
                } else {
                    firestore.collection("trainees")
                        .whereEqualTo("uid", userIdUseCase())
                        .limit(1)
                        .dataObjects<FirestoreTrainee>()
                        .map { trainees ->
                            val trainee = trainees.firstOrNull() ?: FirestoreTrainee()
                            user.toUser(trainee)
                        }
                }
            }.flowOn(Dispatchers.IO)

    override suspend fun getUsersDailyDatas(): Flow<DailyDatas?> =
        firestore.collection("dailyDatas")
            .whereEqualTo("uid", userIdUseCase())
            .limit(1)
            .dataObjects<DailyDatas>()
            .map {
                val result = it.firstOrNull()
                result
            }
            .flowOn(Dispatchers.IO)
    /*override suspend fun getUsersDailyDatas(): Flow<DailyDatas?> {
        val userId = userIdUseCase()
        Log.d("MainServiceOverride", "userId: $userId")
        val query = firestore.collection("dailyDatas")
            .whereEqualTo("uid", userId)
            .limit(1)

        try {
            val snapshot = query.get().await()
            val dailyDatasList = snapshot.toObjects(DailyDatas::class.java)
            Log.d("MainServiceOverride", "dailyDatasList: $dailyDatasList")
        } catch (e: Exception) {
            Log.e("MainServiceOverride", "Error getting daily data: ${e.message}", e)
        }



        return query.dataObjects<DailyDatas>()
            .onEach {
                Log.d("MainServiceOverride", "Inside: $it")
            }
            .map {
                val result = it.firstOrNull()
                Log.d("MainServiceOverride", "InsideOverride: $result")
                result
            }
            .flowOn(Dispatchers.IO)

    }*/
}