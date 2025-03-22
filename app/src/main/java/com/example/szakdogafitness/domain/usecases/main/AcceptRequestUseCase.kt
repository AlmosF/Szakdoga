package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class AcceptRequestUseCase(private val repository: IMainService){
    suspend operator fun invoke(userId: String) =
        repository.acceptRequest(userId)
}