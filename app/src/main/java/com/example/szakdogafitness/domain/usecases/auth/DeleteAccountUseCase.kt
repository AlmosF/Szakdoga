package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class DeleteAccountUseCase(private val repository: AuthService) {
    suspend operator fun invoke() =
        repository.deleteAccount()
}