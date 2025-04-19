package com.example.szakdogafitness.domain.usecases.profile

import com.example.szakdogafitness.data.profile.IProfileService
import com.example.szakdogafitness.domain.model.User

/*
class UpdateUserUseCase(private val repository: IMainService){
    suspend operator fun invoke(user: User) = repository.updateUser(user)
}*/
class UpdateUserUseCase(private val repository: IProfileService){
    suspend operator fun invoke(user: User) = repository.updateUser(user)
}