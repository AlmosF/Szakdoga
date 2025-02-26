package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class SendRecoveryEmailUseCase(private val repository: AuthService) {
    suspend operator fun invoke(email: String) =
        repository.sendRecoveryEmail(email)
}