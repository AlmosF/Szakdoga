package com.example.szakdogafitness.data.auth


import android.content.Context
import com.example.szakdogafitness.domain.model.User

interface AuthService {
    fun currentUserId(): String?

    fun currentUserEmail(): String?

    fun currentUser(): User?

    fun hasUser(): Boolean

    fun isOnline(context: Context): Boolean

    suspend fun signUp(
        email: String, password: String,
    )

    suspend fun signIn(
        email: String,
        password: String
    )

    suspend fun sendRecoveryEmail(email: String)

    suspend fun deleteAccount()

    suspend fun signOut()
}