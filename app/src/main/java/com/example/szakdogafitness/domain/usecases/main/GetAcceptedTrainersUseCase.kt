package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class GetAcceptedTrainersUseCase(private val repository: IMainService){
    suspend fun invoke() =
        repository.getAcceptedTrainers()
}