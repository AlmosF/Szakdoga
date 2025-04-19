package com.example.szakdogafitness.domain.usecases.dailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.google.firebase.Timestamp

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