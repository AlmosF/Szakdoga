package com.example.szakdogafitness.data.profile

import android.net.Uri
import com.example.szakdogafitness.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IProfileService{
    //suspend fun getUser() : Flow<User?>
    suspend fun getUser() : Flow<User?>
    suspend fun updateUser(user: User)
    suspend fun uploadProfilePicture(imageUri: Uri): String


    suspend fun editUser()
}