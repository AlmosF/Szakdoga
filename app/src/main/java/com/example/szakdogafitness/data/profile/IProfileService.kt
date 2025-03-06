package com.example.szakdogafitness.data.profile

import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IProfileService{
    suspend fun getUser() : Flow<User?>
    suspend fun editUser()
}