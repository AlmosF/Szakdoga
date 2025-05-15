package com.example.szakdogafitness.domain.usecases.dailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService

class GenerateNewDailyDataUseCase(private val repository: IDailyDatasService){
    suspend operator fun invoke() =
        repository.generateNewDailyData()
}