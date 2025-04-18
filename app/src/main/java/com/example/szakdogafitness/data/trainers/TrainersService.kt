package com.example.szakdogafitness.data.trainers

import android.util.Log
import com.example.szakdogafitness.domain.model.FirestoreTrainee
import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.model.toUser
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TrainersService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase,
    ) : ITrainersService {

    override suspend fun getTrainers(): Flow<List<User?>> =
        firestore.collection("trainers")
            .dataObjects<FirestoreTrainer>()
            .flatMapLatest { trainerList ->
                val trainerFlows = trainerList.map { trainer ->
                    firestore.collection("users")
                        .whereEqualTo("uid", trainer.uid)
                        .limit(1)
                        .dataObjects<FirestoreUser>()
                        .map { userList ->
                            val user = userList.firstOrNull() ?: FirestoreUser()
                            user.toUser(trainer)
                        }

                }
                combine(trainerFlows) { it.toList() }
            }.flowOn(Dispatchers.IO)

    override suspend fun applyForAppointment(
        trainer: String?,
        appointment: Map<String, List<Int>>,
    ) {
        val userId = userIdUseCase()
        if (trainer == null || userId == null) {
            Log.e("MainService", "Error applying for appointment: Trainer or user ID is null")
            return
        }
        try {
            val requestRef = firestore.collection("requests")
                .document(trainer)
                .collection("traineeRequests")
                .document(userId)

            val request = mapOf(
                "appointment" to appointment,
                "status" to "pending",
                "createdAt" to Timestamp.now(),
                "traineeId" to userId
            )

            requestRef.set(request).await()



        } catch (e: Exception) {
            Log.e("MainService", "Error applying for appointment: ${e.message}", e)
        }
    }

    override suspend fun fetchTrainersRequests(trainerId: String): Flow<Map<String, List<Int>>> = flow {
        try {
            firestore.collection("requests")
                .document(trainerId)
                .collection("traineeRequests")
                .dataObjects<Requests>()
                .collect { traineeRequestsList ->
                    val mergedAppointments = mutableMapOf<String, MutableList<Int>>()

                    traineeRequestsList.forEach { traineeRequests ->
                        traineeRequests.appointment.forEach { (day, hour) ->
                            mergedAppointments.getOrPut(day) { mutableListOf() }.addAll(hour)
                        }
                    }

                    emit(mergedAppointments)


                }
        } catch (e: Exception){
            Log.e("MainService", "Error fetching real-time trainer requests: ${e.message}", e)
            emit(emptyMap())
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun fetchRequestedAppoint(trainerId: String): Flow<Pair<Requests, User>> =
        firestore.collection("requests")
            .document(trainerId)
            .collection("traineeRequests")
            .document(userIdUseCase() ?: "")
            .dataObjects<Requests>()
            .flatMapLatest { requests ->
                if (requests == null) return@flatMapLatest flowOf(Requests() to User())

                getUserById(requests.traineeId)
                    .mapNotNull { user ->
                        if (user != null) requests to user else null
                    }

            }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getUserById(userId: String): Flow<User?> =
        firestore.collection("users")
            .document(userId)
            .dataObjects<FirestoreUser>()
            .flatMapLatest { user->
                if(user != null){
                    if (user.role == 1) {
                        firestore.collection("trainers")
                            .document(userId)
                            .dataObjects<FirestoreTrainer>()
                            .map { trainer ->
                                if (trainer != null) {
                                    user.toUser(trainer)
                                } else {
                                    null
                                }
                            }
                    } else {
                        firestore.collection("trainees")
                            .document(userId)
                            .dataObjects<FirestoreTrainee>()
                            .map { trainee ->
                                if (trainee != null){
                                    user.toUser(trainee)
                                } else {
                                    null
                                }
                            }
                    }
                } else {
                    flowOf(null)
                }
            }.flowOn(Dispatchers.IO)

    override suspend fun getAcceptedTrainers(): Flow<List<Pair<Requests, User>>> =
        firestore.collection("requests")
            .document(userIdUseCase() ?: "")
            .collection("trainers")
            .dataObjects<Requests>()
            .flatMapLatest { requests ->
                Log.d("MainService", "Accepted requests: $requests for ${userIdUseCase()}")
                if (requests.isEmpty()) return@flatMapLatest flowOf(emptyList())

                val acceptedTrainerFlows = requests.map { request ->
                    getUserById(request.trainerId)
                        .mapNotNull { user ->
                            if (user != null) request to user else null
                        }
                }
                combine(acceptedTrainerFlows) { it.toList() }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun rateTrainer(trainerId: String, rate: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun loadTrainersRate(trainerId: String): Flow<Rating> {
        TODO("Not yet implemented")
    }

    override suspend fun loadAllTrainersRates(): Flow<List<Pair<String, List<Rating>>>> {
        TODO("Not yet implemented")
    }

}