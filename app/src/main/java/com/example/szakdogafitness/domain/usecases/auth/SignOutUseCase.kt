package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class SignOutUseCase(private val repository: AuthService) {
    suspend operator fun invoke() =
        repository.signOut()
}