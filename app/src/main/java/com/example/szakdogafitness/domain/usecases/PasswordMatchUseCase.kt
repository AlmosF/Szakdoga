package com.example.szakdogafitness.domain.usecases

class PasswordsMatchUseCase {
    operator fun invoke(password: String, confirmPassword: String): Boolean =
        password == confirmPassword
}