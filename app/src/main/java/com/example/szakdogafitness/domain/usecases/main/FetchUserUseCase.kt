package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.profile.IProfileService

/*
class FetchUserUseCase(private val repository: IMainService){
    suspend operator fun invoke() = repository.getUser()
}*/
class FetchUserUseCase(private val repository: IProfileService){
    suspend operator fun invoke() = repository.getUser()
}