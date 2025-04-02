package com.example.szakdogafitness.data.main

import android.R.attr.rating
import android.util.Log
import android.util.Log.e
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.FirestoreTrainee
import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.model.toFirestoreTrainee
import com.example.szakdogafitness.domain.model.toFirestoreTrainer
import com.example.szakdogafitness.domain.model.toFirestoreUser
import com.example.szakdogafitness.domain.model.toUser
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MainService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase
) : IMainService {

    /*@OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUser(): Flow<User?> =
        firestore.collection("users")
            .whereEqualTo("uid", userIdUseCase())
            .limit(1)
            .dataObjects<FirestoreUser>()
            .flatMapLatest { userList->
                val user = userList.firstOrNull() ?: return@flatMapLatest flowOf(User())
                if (user.role == 1) {
                    firestore.collection("trainers")
                        .whereEqualTo("uid", userIdUseCase())
                        .limit(1)
                        .dataObjects<FirestoreTrainer>()
                        .map { trainers ->
                            val trainer = trainers.firstOrNull() ?: FirestoreTrainer()
                            Log.d("MainService", "Trainer: ${user.toUser(trainer)}")
                            user.toUser(trainer)
                        }
                } else {
                    firestore.collection("trainees")
                        .whereEqualTo("uid", userIdUseCase())
                        .limit(1)
                        .dataObjects<FirestoreTrainee>()
                        .map { trainees ->
                            val trainee = trainees.firstOrNull() ?: FirestoreTrainee()
                            Log.d("MainService", "Trainee: ${user.toUser(trainee)}")
                            user.toUser(trainee)
                        }
                }
            }.flowOn(Dispatchers.IO)*/
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUser(): Flow<User?> =
        firestore.collection("users")
            .document(userIdUseCase() ?: "")
            .dataObjects<FirestoreUser>()
            .flatMapLatest { user->
                if (user == null) return@flatMapLatest flowOf(User())
                //val user = userList.firstOrNull() ?: return@flatMapLatest flowOf(User())
                if (user.role == 1) {
                    firestore.collection("trainers")
                        .whereEqualTo("uid", userIdUseCase())
                        .limit(1)
                        .dataObjects<FirestoreTrainer>()
                        .map { trainers ->
                            val trainer = trainers.firstOrNull() ?: FirestoreTrainer()
                            Log.d("MainService", "Trainer: ${user.toUser(trainer)}")
                            user.toUser(trainer)
                        }
                } else {
                    firestore.collection("trainees")
                        .whereEqualTo("uid", userIdUseCase())
                        .limit(1)
                        .dataObjects<FirestoreTrainee>()
                        .map { trainees ->
                            val trainee = trainees.firstOrNull() ?: FirestoreTrainee()
                            Log.d("MainService", "Trainee: ${user.toUser(trainee)}")
                            user.toUser(trainee)
                        }
                }
            }.flowOn(Dispatchers.IO)


    /*override suspend fun fetchPendingRequests(status: String): Flow<List<Pair<Requests, User>>> =
        firestore.collection("requests")
            .document(userIdUseCase() ?: "")
            .collection("traineeRequests")
            .whereEqualTo("status", status)
            .dataObjects<Requests>()
            .flatMapLatest { requests ->
                val requestFlows = requests.map { request ->
                    getUserById(request.traineeId)
                        .mapNotNull { user ->
                            if (user != null) {
                                request to user
                            } else {
                                null
                            }
                        }
                }
                combine(requestFlows) { it.toList() }
            }.flowOn(Dispatchers.IO)*/



    /*override suspend fun fetchPendingRequests(status: String): Flow<List<Requests>> =
        firestore.collection("requests")
            .document(userIdUseCase()!!)
            .collection("traineeRequests")
            .whereEqualTo("status", status)
            .dataObjects<Requests>()
            .flowOn(Dispatchers.IO)*/


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

    override suspend fun updateUser(user: User) {
        val currentUserId = userIdUseCase()
        val updatedFirestoreUser = user.toFirestoreUser()

        firestore.collection("users")
            .document(currentUserId!!)
            .set(updatedFirestoreUser)
            .await()

        if (user.role == 1) {
            val updatedFirestoreTrainer = user.toFirestoreTrainer()
            firestore.collection("trainers")
                .document(currentUserId)
                .set(updatedFirestoreTrainer)
                .await()
        } else {
            val updatedFirestoreTrainee = user.toFirestoreTrainee()
            firestore.collection("trainees")
                .document(currentUserId)
                .set(updatedFirestoreTrainee)
                .await()
        }

    }

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

    /*override suspend fun getUsersDailyDatas(): Flow<DailyDatas?> =
        firestore.collection("dailyDatas")
            .whereEqualTo("uid", userIdUseCase())
            .limit(1)
            .dataObjects<DailyDatas>()
            .map {
                val result = it.firstOrNull()
                Log.d("MainService", "Inside: $result")
                Log.d("MainService", "Inside: ${userIdUseCase()}")
                result
            }
            .flowOn(Dispatchers.IO)*/

    /*override suspend fun getUsersDailyDatas(): Flow<DailyDatas?> = //Fixme("WorkingOne")
        flow {
            val userId = userIdUseCase()
            Log.d("MainService", "userId: $userId")
            firestore.collection("dailyDatas")
                .whereEqualTo("uid", userIdUseCase())
                .limit(1)
                .dataObjects<DailyDatas>()
                .collect{ list ->
                    val result = list.firstOrNull()
                    Log.d("MainService", "Inside: $result")
                    emit(result)
                }
                /*.map {
                    val result = it.firstOrNull()
                    Log.d("MainService", "Inside: $result")
                    Log.d("MainService", "Inside: ${userIdUseCase()}")
                    emit(result)
                }*/

        }.flowOn(Dispatchers.IO)*/

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

    override suspend fun fetchRequestedAppointments(trainerId: String): Flow<Map<String, List<Int>>> = flow {
        val userId = userIdUseCase()
        if (userId == null) {
            Log.e("MainService", "Error fetching requested appointments: User ID is null")
            return@flow
        }
        Log.d("MainService", "Fetching requested appointments for user: $userId")
        try {
            firestore.collection("requests")
                .document(trainerId)
                .collection("traineeRequests")
                .document(userId)
                .dataObjects<Requests>()
                .collect { requests ->
                    if (requests != null) {
                        Log.d("MainService", "--- Requests: $requests")
                        emit(requests.appointment)
                    } else {
                        Log.d("MainService", "No requested appointments found for user: $userId at trainer: $trainerId")
                        emit(emptyMap())
                    }
                }

        } catch (e: Exception){
            Log.e("MainService", "Error fetching requested appointments: ${e.message}", e)
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


    override suspend fun deleteRequest(traineeId: String) {
        val userId = userIdUseCase()
        if (userId == null) {
            Log.e("MainService", "Error deleting request: User ID is null")
            return
        }
        try {
            firestore.collection("requests")
                .document(userId)
                .collection("traineeRequests")
                .document(traineeId)
                .delete()
                .await()



        } catch (e: Exception) {
            Log.e("MainService", "Error deleting request: ${e.message}", e)
        }
    }

    override suspend fun acceptRequest(traineeId: String) {
        val userId = userIdUseCase()
        if (userId == null) {
            Log.e("MainService", "Error accepting request: User ID is null")
            return
        }
        try {
            val requestRef = firestore.collection("requests")
                .document(userId)
                .collection("traineeRequests")
                .document(traineeId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(requestRef)

                val request = snapshot.toObject(Requests::class.java)

                if (request != null) {
                    transaction.update(requestRef, "status", "accepted")
                }
            }.await()
            val appointment = requestRef
                .get()
                .await()
                .toObject(Requests::class.java)
                ?.appointment
            // Create a new document for trainee->trainer
            val traineeRef = firestore.collection("requests")
                .document(traineeId)
                .collection("trainers")
                .document(userId)

            val request = mapOf(
                "appointment" to appointment,
                "status" to "accepted",
                "createdAt" to Timestamp.now(),
                "trainerId" to userId
            )

            traineeRef.set(request).await()


        } catch (e: Exception) {
            Log.e("MainService", "Error accepting request: ${e.message}", e)
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

    override suspend fun fetchRequests(status: String): Flow<List<Pair<Requests, User>>> =
        firestore.collection("requests")
            .document(userIdUseCase() ?: "")
            .collection("traineeRequests")
            .whereEqualTo("status", status)
            .dataObjects<Requests>()
            .flatMapLatest { requests ->
                if (requests.isEmpty()) return@flatMapLatest flowOf(emptyList()) // 🔥 Emit immediately if empty

                val requestFlows = requests.map { request ->
                    getUserById(request.traineeId)
                        .mapNotNull { user ->
                            if (user != null) request to user else null
                        }
                }
                combine(requestFlows) { it.toList() }
            }
            .flowOn(Dispatchers.IO)

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



    /*override suspend fun generateNewDailyData(dailyDatas: DailyDatas) {
        val userId = userIdUseCase()
        val todayDate = Timestamp.now().toYYYYMMDD()

        val query = firestore.collection("dailyDatas")
            .whereEqualTo("uid", userId)
            .limit(1)

        try {
            val snapshot = query.get().await()
            val existingDailyDatas = snapshot.toObjects(DailyDatas::class.java).first()
            val hasTodayData = existingDailyDatas.dailyData.any{ it.date.toYYYYMMDD() == todayDate}

            if (!hasTodayData){
                val newDailyData = DailyData(date = Timestamp.now())
                val updatedDailyDataList = existingDailyDatas.dailyData + newDailyData

                firestore.collection("dailyDatas")
                    .document(snapshot.documents.first().id)
                    .update("dailyData", updatedDailyDataList)
                    .await()

                Log.d("MainService", "Updated daily data for user: $userId")
            }

        } catch (e: Exception) {
            Log.e("MainService", "Error generating new daily data: ${e.message}", e)
        }
    }*/
    override suspend fun generateNewDailyData() {
        val userId = userIdUseCase()

        // Format today's date as "YYYY-MM-DD"
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        Log.d("MainService", "Generating new daily data for user: $userId")

        try {
            val userDocRef = firestore.collection("dailyDatas").document(userId!!)
            val dailyDataCollectionRef = userDocRef.collection("dailyData")

            // Check if today's entry exists
            val existingDoc = dailyDataCollectionRef.document(todayDate).get().await()

            if (!existingDoc.exists()) {
                val newDailyData = DailyData(date = Timestamp.now())

                // Create the new daily entry with today's date as the document ID
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

    override suspend fun rateTrainer(trainerId: String, rate: Int) {
        val userId = userIdUseCase()
        if (userId == null) {
            Log.e("MainService", "Error rating trainer: User ID is null")
            return
        }
        try {
            val ratingRef = firestore.collection("ratings")
                .document(trainerId)
                .collection("trainerRatings")
                .document(userId)

            /*val rating = Rating(
                rating = rate,
                timestamp = Timestamp.now()
            )*/
            val rating = mapOf(
                "rating" to rate,
                "timestamp" to Timestamp.now()
            )
            ratingRef.set(rating).await()

        } catch (e: Exception) {
            Log.e("MainService", "Error rating trainer: ${e.message}", e)
        }
    }

    override suspend fun loadTrainersRate(trainerId: String): Flow<Rating> =
        firestore.collection("ratings")
            .document(trainerId)
            .collection("trainerRatings")
            .document(userIdUseCase() ?: "")
            .dataObjects<Rating>()
            .map { it ?: Rating() }
            .flowOn(Dispatchers.IO)

    override suspend fun loadAllTrainersRates(): Flow<List<Pair<String, List<Rating>>>> = flow {
        Log.d("MainService", "Fetching all trainers' ratings")

        val ratingQuerySnapshot = firestore.collectionGroup("trainerRatings").get().await()

        if (ratingQuerySnapshot.isEmpty) {
            Log.d("MainService", "No ratings found.")
            emit(emptyList())
            return@flow
        }

        val trainerFlows = ratingQuerySnapshot.documents.map { ratingDoc ->
            val trainerId = ratingDoc.reference.parent.parent?.id ?: ""

            firestore.collection("ratings")
                .document(trainerId)
                .collection("trainerRatings")
                .dataObjects<Rating>()
                .map { ratings ->
                    trainerId to ratings
                }
        }

        emitAll(combine(trainerFlows) { results ->
            results.distinctBy { it.first }
        })
    }.flowOn(Dispatchers.IO)




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