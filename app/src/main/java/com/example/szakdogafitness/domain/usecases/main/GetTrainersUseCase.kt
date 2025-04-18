package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.trainers.ITrainersService

/*
class GetTrainersUseCase(private val repository: IMainService){
    suspend operator fun invoke() = repository.getTrainers()
}*/
class GetTrainersUseCase(private val repository: ITrainersService){
    suspend operator fun invoke() = repository.getTrainers()
}