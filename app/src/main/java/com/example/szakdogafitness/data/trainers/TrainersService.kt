package com.example.szakdogafitness.data.trainers

import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.model.toUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrainersService @Inject constructor(
    private val firestore: FirebaseFirestore,
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
        appointment: Map<String, List<Int>>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchTrainersRequests(trainerId: String): Flow<Map<String, List<Int>>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRequestedAppoint(trainerId: String): Flow<Pair<Requests, User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAcceptedTrainers(): Flow<List<Pair<Requests, User>>> {
        TODO("Not yet implemented")
    }

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