package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class CurrentUserIdUseCase(private val repository: AuthService){
    operator fun invoke() = repository.currentUserId()
}