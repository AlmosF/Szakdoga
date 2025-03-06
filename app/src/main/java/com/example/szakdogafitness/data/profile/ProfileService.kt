package com.example.szakdogafitness.data.profile

import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.model.toUser
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase,
) : IProfileService{

    /*override suspend fun getUser() : Flow<User> {
        if (userIdUseCase() != null){
            Log.d("ProfileService", "User found: ${userIdUseCase()}")
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("uid", userIdUseCase())
                .limit(1)
                .get()
                .await()
            Log.d("ProfileService", "User found: ${querySnapshot.documents}")
            val fireStoreUser = querySnapshot.documents.firstOrNull()?.toObject(FirestoreUser::class.java)
            return fireStoreUser
        } else {
            Log.d("ProfileService", "User not found: ${userIdUseCase()}")
            return User()
        }
    }*/
    /*override suspend fun getUser(): Flow<User> =
        firestore.collection("users")
            .whereEqualTo("uid", userIdUseCase())
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.toObject(FirestoreUser::class.java)
            ?.let { flowOf(it.toUser(FirestoreTrainer())) }
            ?: flowOf(User())*/

    override suspend fun getUser(): Flow<User?> =
        firestore.collection("users")
            .whereEqualTo("uid", userIdUseCase())
            .limit(1)
            //.snapshots()
            .dataObjects<FirestoreUser>()
            .map { list ->
                list.firstOrNull()?.toUser(FirestoreTrainer())
            }
            /*.map { list -> //ez jo volt
                val fireStoreUser = list.firstOrNull()
                fireStoreUser?.toUser(FirestoreTrainer()) ?: User()
            }*/
            /*.map { querySnapshot ->
                val fireStoreUser = querySnapshot.documents.firstOrNull()?.toObject(FirestoreUser::class.java)
                fireStoreUser?.toUser(FirestoreTrainer()) ?: User()
            }*/


    override suspend fun editUser() {
        TODO("Not yet implemented")
    }


}