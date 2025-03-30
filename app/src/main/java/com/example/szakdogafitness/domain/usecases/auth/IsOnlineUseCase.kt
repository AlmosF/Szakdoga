package com.example.szakdogafitness.domain.usecases.auth

import android.content.Context
import com.example.szakdogafitness.data.auth.AuthService

class IsOnlineUseCase(private val repository: AuthService){
    operator fun invoke(context: Context) = repository.isOnline(context)
}