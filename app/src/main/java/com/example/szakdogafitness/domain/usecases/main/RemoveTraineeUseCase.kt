package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService


class RemoveTraineeUseCase(private val repository: IMainService){
    suspend operator fun invoke(traineeId: String) = repository.removeTrainee(traineeId)
}