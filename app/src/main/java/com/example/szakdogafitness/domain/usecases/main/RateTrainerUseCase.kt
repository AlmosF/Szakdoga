package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class RateTrainerUseCase(private val repository: IMainService){
    suspend operator fun invoke(trainerId: String, rate: Int){
        repository.rateTrainer(trainerId, rate)
    }
}