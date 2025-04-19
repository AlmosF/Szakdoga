package com.example.szakdogafitness.domain.usecases.dailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService

/*
class GenerateNewDailyDataUseCase(private val repository: IMainService){
    //suspend operator fun invoke(dailyDatas: DailyDatas) = repository.generateNewDailyData(dailyDatas) //fixme("Workingone")
    suspend operator fun invoke() =
        repository.generateNewDailyData()
}*/
class GenerateNewDailyDataUseCase(private val repository: IDailyDatasService){
    suspend operator fun invoke() =
        repository.generateNewDailyData()
}