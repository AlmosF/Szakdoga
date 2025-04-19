package com.example.szakdogafitness.domain.usecases.trainers

import com.example.szakdogafitness.data.trainers.ITrainersService

/*
class GetAcceptedTrainersUseCase(private val repository: IMainService){
    suspend fun invoke() =
        repository.getAcceptedTrainers()
}*/
class GetAcceptedTrainersUseCase(private val repository: ITrainersService){
    suspend fun invoke() =
        repository.getAcceptedTrainers()
}