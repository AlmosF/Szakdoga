package com.example.szakdogafitness.data.stepcount

interface IStepCountRepository {
    suspend fun saveStepsLocally(steps: Int)
    suspend fun getSteps(): Int
    suspend fun resetSteps()
    suspend fun syncStepsToFirestore()
    suspend fun saveInitialSensorSteps(steps: Int)
    suspend fun getInitialSensorSteps(): Int
}