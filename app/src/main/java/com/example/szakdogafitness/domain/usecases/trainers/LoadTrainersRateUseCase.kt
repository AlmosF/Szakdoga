package com.example.szakdogafitness.domain.usecases.trainers

import com.example.szakdogafitness.data.trainers.ITrainersService

/*class LoadTrainersRateUseCase(private val repository: IMainService){
    suspend operator fun invoke(trainerId: String) =
        repository.loadTrainersRate(trainerId)
}*/
class LoadTrainersRateUseCase(private val repository: ITrainersService){
    suspend operator fun invoke(trainerId: String) =
        repository.loadTrainersRate(trainerId)
}

/*class LoadAllTrainersAllRateUseCase(private val repository: IMainService){
    suspend operator fun invoke() =
        repository.loadAllTrainersRates()
}*/
