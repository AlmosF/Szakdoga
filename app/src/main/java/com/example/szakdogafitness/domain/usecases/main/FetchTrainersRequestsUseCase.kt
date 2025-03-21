package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class FetchTrainersRequestsUseCase(private val repository: IMainService){
    suspend operator fun invoke(trainerId: String) =
        repository.fetchTrainersRequests(trainerId)
}