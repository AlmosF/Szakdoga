package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class LoadTrainersRateUseCase(private val repository: IMainService){
    suspend operator fun invoke(trainerId: String) =
        repository.loadTrainersRate(trainerId)
}
