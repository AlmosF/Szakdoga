package com.example.szakdogafitness.domain.usecases.main

import android.util.Log
import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.trainees.ITraineesService

/*class FetchUsersDailyDatasUseCase (private val repository: IMainService){
    suspend operator fun invoke() =
        repository.getUsersDailyDatas()
}*/
class FetchUsersDailyDatasUseCase (private val repository: IDailyDatasService){
    suspend operator fun invoke() =
        repository.getUsersDailyDatas()
}

/*
class FetchUsersDailyDatasByIdUseCase (private val repository: IMainService){
    suspend operator fun invoke(userId: String) =
        repository.getUsersDailyDatasById(userId)
}*/
class FetchUsersDailyDatasByIdUseCase (private val repository: ITraineesService){
    suspend operator fun invoke(userId: String) =
        repository.getUsersDailyDatasById(userId)
}