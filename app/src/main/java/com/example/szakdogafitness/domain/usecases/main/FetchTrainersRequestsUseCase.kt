package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.trainers.ITrainersService

/*
class FetchTrainersRequestsUseCase(private val repository: IMainService){
    suspend operator fun invoke(trainerId: String) =
        repository.fetchTrainersRequests(trainerId)
}*/
class FetchTrainersRequestsUseCase(private val repository: ITrainersService){
    suspend operator fun invoke(trainerId: String) =
        repository.fetchTrainersRequests(trainerId)
}