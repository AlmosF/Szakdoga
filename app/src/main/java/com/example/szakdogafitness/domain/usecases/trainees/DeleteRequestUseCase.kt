package com.example.szakdogafitness.domain.usecases.trainees

import com.example.szakdogafitness.data.trainees.ITraineesService

class DeleteRequestUseCase(private val repository: ITraineesService){
    suspend operator fun invoke(traineeId: String) = repository.deleteRequest(traineeId)
}