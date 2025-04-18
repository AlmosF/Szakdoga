package com.example.szakdogafitness.domain.usecases.mealLog

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.example.szakdogafitness.data.main.IMainService
import com.google.firebase.Timestamp

/*
class TargetKcalModifyUseCase(private val repository: IMainService){
    suspend operator fun invoke(
        targetKcal: Int,
        date: Timestamp
    ) =
        repository.modifyTargetKcal(
            targetKcal,
            date
        )
}*/
class TargetKcalModifyUseCase(private val repository: IDailyDatasService){
    suspend operator fun invoke(
        targetKcal: Int,
        date: Timestamp
    ) =
        repository.modifyTargetKcal(
            targetKcal,
            date
        )
}