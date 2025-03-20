package com.example.szakdogafitness

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltAndroidApp
class FitnessApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        resetFirestoreCache()
    }
    //Needed incase of cache problems
    private fun resetFirestoreCache() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val firestore = FirebaseFirestore.getInstance()
                firestore.terminate().await()
                firestore.clearPersistence().await()
                firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
                firestore
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}