package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.trainees.ITraineesService

/*
class AcceptRequestUseCase(private val repository: IMainService){
    suspend operator fun invoke(userId: String) =
        repository.acceptRequest(userId)
}*/
class AcceptRequestUseCase(private val repository: ITraineesService){
    suspend operator fun invoke(userId: String) =
        repository.acceptRequest(userId)
}