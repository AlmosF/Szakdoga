package com.example.szakdogafitness.datastore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun syncSteps(userId: String, date: String, steps: Int) {
        Log.d("FirestoreDataSource", "Syncing steps for user: $userId, date: $date, steps: $steps")
        val docRef = firestore.collection("dailyDatas")
            .document(userId)
            .collection("dailyData")
            .document(date)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val existing = snapshot.getLong("steps") ?: 0
            transaction.set(docRef, mapOf("steps" to existing + steps), SetOptions.merge())
        }.await()
    }
}