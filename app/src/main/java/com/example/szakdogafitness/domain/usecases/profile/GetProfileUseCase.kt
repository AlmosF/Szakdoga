package com.example.szakdogafitness.domain.usecases.profile

import com.example.szakdogafitness.data.profile.IProfileService

class GetProfileUseCase(private val repository: IProfileService){
    suspend operator fun invoke() = repository.getUser()
}