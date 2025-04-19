package com.example.szakdogafitness.domain.usecases.trainers

import com.example.szakdogafitness.data.trainers.ITrainersService

/*
class RateTrainerUseCase(private val repository: IMainService){
    suspend operator fun invoke(trainerId: String, rate: Int){
        repository.rateTrainer(trainerId, rate)
    }
}*/
class RateTrainerUseCase(private val repository: ITrainersService){
    suspend operator fun invoke(trainerId: String, rate: Int){
        repository.rateTrainer(trainerId, rate)
    }
}