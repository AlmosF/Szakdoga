package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class SignUpUseCase(private val repository: AuthService) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signUp(email, password)
}