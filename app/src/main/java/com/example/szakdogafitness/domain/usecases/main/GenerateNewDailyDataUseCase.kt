package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.domain.model.DailyDatas

class GenerateNewDailyDataUseCase(private val repository: IMainService){
    suspend operator fun invoke(dailyDatas: DailyDatas) =
        repository.generateNewDailyData(dailyDatas)
}