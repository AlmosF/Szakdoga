package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class DeleteRequestUseCase(private val repository: IMainService){
    suspend operator fun invoke(traineeId: String) = repository.deleteRequest(traineeId)
}