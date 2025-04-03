package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.google.firebase.Timestamp

class WaterIntakeModifyUseCase(private val repository: IMainService){
    suspend operator fun invoke(
        waterIntake: Double,
        date: Timestamp
    ) =
        repository.updateWaterIntake(
            waterIntake,
            date
        )
}