package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMain

class FetchTraineesUseCase(private val repository: IMain){
    suspend operator fun invoke() = repository.fetchTrainees()
}