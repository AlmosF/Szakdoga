package com.example.szakdogafitness.data.profile

import android.net.Uri
import android.util.Log
import com.example.szakdogafitness.domain.model.FirestoreTrainee
import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.model.toFirestoreTrainee
import com.example.szakdogafitness.domain.model.toFirestoreTrainer
import com.example.szakdogafitness.domain.model.toFirestoreUser
import com.example.szakdogafitness.domain.model.toUser
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase,
    private val storage: FirebaseStorage
) : IProfileService{

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

    override suspend fun uploadProfilePicture(imageUri: Uri): String {
        val userId = userIdUseCase()
        val storageRef = storage.reference.child("profile_pictures/$userId.jpg")
        storageRef.putFile(imageUri).await()

        val downloadUrl = storageRef.downloadUrl.await().toString()
        firestore.collection("users").document(userId!!)
            .update("profileImageUrl", downloadUrl)
            .await()
        return downloadUrl
    }

    override suspend fun editUser() {
        TODO("Not yet implemented")
    }


}