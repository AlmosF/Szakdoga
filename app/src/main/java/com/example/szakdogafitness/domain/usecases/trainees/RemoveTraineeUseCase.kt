package com.example.szakdogafitness.domain.usecases.trainees

import com.example.szakdogafitness.data.trainees.ITraineesService

/*
class RemoveTraineeUseCase(private val repository: IMainService){
    suspend operator fun invoke(traineeId: String) = repository.removeTrainee(traineeId)
}*/
class RemoveTraineeUseCase(private val repository: ITraineesService){
    suspend operator fun invoke(traineeId: String) = repository.removeTrainee(traineeId)
}