package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class CurrentUserEmailUseCase(private val repository: AuthService){
    operator fun invoke() = repository.currentUserEmail()
}