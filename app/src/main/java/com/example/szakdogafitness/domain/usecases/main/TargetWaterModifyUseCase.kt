package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.example.szakdogafitness.data.main.IMainService
import com.google.firebase.Timestamp

/*
class TargetWaterModifyUseCase(private val repository: IMainService){
    suspend operator fun invoke(
        targetWater: Double,
        date: Timestamp
    ) =
        repository.modifyTargetWater(
            targetWater,
            date
        )
}*/

class TargetWaterModifyUseCase(private val repository: IDailyDatasService){
    suspend operator fun invoke(
        targetWater: Double,
        date: Timestamp
    ) =
        repository.modifyTargetWater(
            targetWater,
            date
        )
}