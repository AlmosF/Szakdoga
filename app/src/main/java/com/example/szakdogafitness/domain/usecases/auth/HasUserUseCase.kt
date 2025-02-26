package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class HasUserUseCase(private val repository: AuthService) {
    operator fun invoke() =
        repository.hasUser()
}