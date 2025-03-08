package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMain

class FetchTrainersUseCase(private val repository: IMain){
    suspend operator fun invoke() = repository.fetchTrainers()
}