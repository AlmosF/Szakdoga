package com.example.szakdogafitness.domain.usecases.dailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService

class FetchUsersDailyDatasUseCase (private val repository: IDailyDatasService){
    suspend operator fun invoke() =
        repository.getUsersDailyDatas()
}