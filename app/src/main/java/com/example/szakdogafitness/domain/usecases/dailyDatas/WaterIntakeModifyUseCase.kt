package com.example.szakdogafitness.domain.usecases.dailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.google.firebase.Timestamp

/*
class WaterIntakeModifyUseCase(private val repository: IMainService){
    suspend operator fun invoke(
        waterIntake: Double,
        date: Timestamp
    ) =
        repository.updateWaterIntake(
            waterIntake,
            date
        )
}*/
class WaterIntakeModifyUseCase(private val repository: IDailyDatasService){
    suspend operator fun invoke(
        waterIntake: Double,
        date: Timestamp
    ) =
        repository.updateWaterIntake(
            waterIntake,
            date
        )
}