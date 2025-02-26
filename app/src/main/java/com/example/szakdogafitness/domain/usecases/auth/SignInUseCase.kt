package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class SignInUseCase(private val repository: AuthService) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signIn(email, password)
}