package com.example.szakdogafitness.data.main

import android.R.attr.rating
import android.util.Log
import android.util.Log.e
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.FirestoreTrainee
import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.Trainee
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.example.szakdogafitness.domain.usecases.profile.GetProfileByIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


class Main @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase,
    private val profileByIdUseCase: GetProfileByIdUseCase
) : IMain {


    override suspend fun fetchDailyData(): Flow<DailyDatas> {
        Log.d("MAINCLASS", "fetchDailyData")
        return firestore.collection(DAILY_DATA_COLLECTION)
            .whereEqualTo("uid", userIdUseCase())
            .limit(1)
            .dataObjects<DailyDatas>()
            .map {
                it.firstOrNull() ?: DailyDatas(
                    uid = userIdUseCase()!!,
                )
            }
    }

    /*override suspend fun fetchTrainers(): Flow<List<User>> {
        val trainers = firestore.collection(TRAINERS_COLLECTION)
            .dataObjects<FirestoreTrainer>()

        val users = firestore.collection(USERS_COLLECTION)
            .whereEqualTo("uid", profileByIdUseCase(
                trainers.map { }
            ))
            .dataObjects<FirestoreUser>()


    }*/
    override suspend fun fetchTrainers(): Flow<List<User>> = flow {
        try {
            val trainers = firestore.collection(TRAINERS_COLLECTION)
                .dataObjects<FirestoreTrainer>()
                .first()

            trainers.forEach {
                Log.d("MainNavigationFIND", it.toString())
            }

            val trainerUids = trainers.map { it.uid }

            if (trainerUids.isEmpty()) {
                emit(emptyList())
                return@flow
            }

            val users = firestore.collection(USERS_COLLECTION)
                .whereIn("uid", trainerUids)
                .dataObjects<FirestoreUser>()
                .first()

            val mergedUsers = users.map { user ->
                User(
                    userId = user.uid,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    dateOfBirth = user.dateOfBirth,
                    gender = user.gender,
                    phoneNumber = user.phoneNumber,
                    role = user.role,
                    bio = user.bio,
                    fcmToken = user.fcmToken,
                    profileImageUrl = user.profileImageUrl,
                    location = user.location,
                    trainer = trainers.find { it.uid == user.uid }?.let { firestoreTrainer ->
                        Trainer(
                            certifications = firestoreTrainer.certifications,
                            areasOfExperties = firestoreTrainer.areasOfExperties,
                            trainingStyles = firestoreTrainer.trainingStyles,
                            experience = firestoreTrainer.experience,
                            availability = firestoreTrainer.availability,
                            pricing = firestoreTrainer.pricing,
                            rating = firestoreTrainer.rating,
                            specializations = firestoreTrainer.specializations,
                            socialMediaLinks = firestoreTrainer.socialMediaLinks,
                            pendingRequests = firestoreTrainer.pendingRequests,
                            acceptedRequests = firestoreTrainer.acceptedRequests
                        )
                    } ?: Trainer(),
                    isSetupDone = user.setupDone
                )
            }

            mergedUsers.forEach {
                Log.d("MainNavigation", it.trainer.rating.toString())
            }

            emit(mergedUsers)

        } catch (e: Exception) {
            emit(emptyList())
        }
    }


    override suspend fun fetchTrainees(): Flow<List<User>> = flow {
        try {
            val trainees = firestore.collection(TRAINEES_COLLECTION)
                .dataObjects<FirestoreTrainee>()
                .first()

            val traineeUids = trainees.map { it.uid }

            if (traineeUids.isEmpty()) {
                emit(emptyList())
                return@flow
            }

            val users = firestore.collection(USERS_COLLECTION)
                .whereIn("uid", traineeUids)
                .dataObjects<FirestoreUser>()
                .first()

            val mergedUsers = users.map { user ->
                User(
                    userId = user.uid,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    dateOfBirth = user.dateOfBirth,
                    gender = user.gender,
                    phoneNumber = user.phoneNumber,
                    role = user.role,
                    bio = user.bio,
                    fcmToken = user.fcmToken,
                    profileImageUrl = user.profileImageUrl,
                    location = user.location,
                    trainee = trainees.find { it.uid == user.uid }?.let { firestoreTrainee ->
                        Trainee(
                            unit = firestoreTrainee.unit,
                            height = firestoreTrainee.height,
                            weight = firestoreTrainee.weight,
                            trainings = firestoreTrainee.trainings,
                            fitnessGoals = firestoreTrainee.fitnessGoals,
                            currentFitnessLevel = firestoreTrainee.currentFitnessLevel,
                            trainingPreference = firestoreTrainee.trainingPreference,
                            healthInformation = firestoreTrainee.healthInformation,
                            dietaryPreferences = firestoreTrainee.dietaryPreferences,
                            availableEquipment = firestoreTrainee.availableEquipment,
                            activeTrainers = firestoreTrainee.activeTrainers
                        )
                    } ?: Trainee(),
                    isSetupDone = user.setupDone
                )
            }

            Log.d("MainNavigation", mergedUsers.toString())

            emit(mergedUsers)

        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    /*override suspend fun addNewDailyData(
        current: DailyDatas, newDay: DailyData
    ) {
        val updatedDailyDatas = current.dailyData.toMutableList()

        val newDayFormatted = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(newDay.date.toDate())

        val dateExists = updatedDailyDatas.any {
            val existingDayFormatted = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.date.toDate())
            existingDayFormatted == newDayFormatted
        }

        if (!dateExists) {
            updatedDailyDatas.add(newDay)
            firestore.collection(DAILY_DATA_COLLECTION)
                .whereEqualTo("uid", current.uid)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.reference
                ?.set(current.copy(dailyData = updatedDailyDatas))
        }
    }*/
    override suspend fun addNewDailyData(
        current: DailyDatas, newDay: DailyData
    ) {

        val updatedDailyDatas = current.dailyData.toMutableList()

        val newDayFormatted = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(newDay.date.toDate())

        val dateExists = updatedDailyDatas.any {
            val existingDayFormatted = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(it.date.toDate())
            existingDayFormatted == newDayFormatted
        }

        if (!dateExists) {
            updatedDailyDatas.add(newDay)

            val userDocument = firestore.collection(DAILY_DATA_COLLECTION)
                .whereEqualTo("uid", userIdUseCase())
                .get()
                .await()
                .documents
                .firstOrNull()

            if (userDocument == null) {
                // If no document exists, create one
                firestore.collection(DAILY_DATA_COLLECTION)
                    .add(current.copy(dailyData = updatedDailyDatas))
                    .await()
            } else {
                // If document exists, update it
                userDocument.reference
                    .set(current.copy(dailyData = updatedDailyDatas))
                    .await()
            }
        }
    }



    override suspend fun editDailyData(dailyData: DailyData): Flow<DailyDatas> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DAILY_DATA_COLLECTION = "dailyDatas"
        private const val TRAINERS_COLLECTION = "trainers"
        private const val TRAINEES_COLLECTION = "trainees"
        private const val USERS_COLLECTION = "users"
    }

}

