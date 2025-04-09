package com.example.szakdogafitness.data.stepcount

import android.util.Log
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.szakdogafitness.datastore.FirestoreDataSource
import com.example.szakdogafitness.datastore.StepDataStore
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class StepCountRepository @Inject constructor(
    private val dataStore: StepDataStore,
    private val firestore: FirestoreDataSource,
    private val userIdUseCase: CurrentUserIdUseCase
): IStepCountRepository {

    override suspend fun saveStepsLocally(steps: Int) {
        val current = dataStore.getSteps()
        dataStore.saveSteps(steps)
        Log.d("StepCountRepository", "Saving steps locally: $steps, current: $current")
    }

    override suspend fun getSteps(): Int = dataStore.getSteps()

    override suspend fun resetSteps() {
        Log.d("StepCountRepository", "Resetting steps")
        dataStore.resetSteps()
        Log.d("StepCountRepository", "${getSteps()}")
    }

    override suspend fun syncStepsToFirestore() {
        val userId = userIdUseCase() ?: return
        val steps = getSteps()
        if (steps > 0) {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Timestamp.now().toDate())
            firestore.syncSteps(userId, date, steps)
            resetSteps()
            dataStore.setLastSyncDate(date)
            // Reset initial steps after sync
            dataStore.saveInitialSensorSteps(-1)
        }
    }
    override suspend fun saveInitialSensorSteps(steps: Int) {
        dataStore.saveInitialSensorSteps(steps)
    }

    override suspend fun getInitialSensorSteps(): Int {
        return dataStore.getInitialSensorSteps()
    }
}