package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.domain.model.User

class UpdateUserUseCase(private val repository: IMainService){
    suspend operator fun invoke(user: User) = repository.updateUser(user)
}