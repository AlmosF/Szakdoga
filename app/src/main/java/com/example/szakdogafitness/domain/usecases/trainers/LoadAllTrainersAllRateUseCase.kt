package com.example.szakdogafitness.domain.usecases.trainers

import com.example.szakdogafitness.data.trainers.ITrainersService

class LoadAllTrainersAllRateUseCase(private val repository: ITrainersService){
    suspend operator fun invoke() =
        repository.loadAllTrainersRates()
}
