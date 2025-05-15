package com.example.szakdogafitness.domain.usecases.trainees

import com.example.szakdogafitness.data.trainees.ITraineesService

class AcceptRequestUseCase(private val repository: ITraineesService){
    suspend operator fun invoke(userId: String) =
        repository.acceptRequest(userId)
}