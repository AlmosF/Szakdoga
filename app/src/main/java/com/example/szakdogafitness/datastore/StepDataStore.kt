package com.example.szakdogafitness.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StepDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "step_prefs")

    private val stepsKey = intPreferencesKey("daily_steps")
    private val lastSyncDateKey = stringPreferencesKey("last_sync_date")

    suspend fun saveSteps(steps: Int) {
        context.dataStore.edit { it[stepsKey] = steps }
    }

    suspend fun getSteps(): Int {
        return context.dataStore.data.first()[stepsKey] ?: 0
    }

    suspend fun resetSteps() {
        context.dataStore.edit { it[stepsKey] = 0 }
    }

    suspend fun getLastSyncDate(): String {
        return context.dataStore.data.first()[lastSyncDateKey] ?: ""
    }

    suspend fun setLastSyncDate(date: String) {
        context.dataStore.edit { it[lastSyncDateKey] = date }
    }
    private val initialStepsKey = intPreferencesKey("initial_sensor_steps")

    suspend fun saveInitialSensorSteps(steps: Int) {
        context.dataStore.edit { it[initialStepsKey] = steps }
    }

    suspend fun getInitialSensorSteps(): Int {
        return context.dataStore.data.first()[initialStepsKey] ?: -1
    }
}