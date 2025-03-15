package com.example.szakdogafitness.viewmodel

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.main.UpdateUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.AllProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: AllProfileUseCases,
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()


    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.EditUser -> {
                _state.update { it.copy(editUser = event.user) }
            }
            ProfileEvent.GetUser -> {
                Log.d("ProfileViewModel", "onEvent: GetUser")
                getUser()
            }

            ProfileEvent.SwitchEdit -> {
                Log.e("ProfileViewModel", "ARE YOU IN A NEW PROFILE????")
                _state.update { it.copy(
                    switchEdit = !it.switchEdit,
                    editUser = it.user,
                ) }
            }

            ProfileEvent.UpdateUser -> {
                updateUser()
            }
        }
    }

    private fun updateUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val user = _state.value.editUser ?: return@launch
                updateUserUseCase(user)
                _state.update { it.copy(switchEdit = false) }
            } catch (e: Exception){
                Log.e("ProfileViewModel", "updateUser: ${e.message}")
                _state.update { it.copy(isLoading = false, switchEdit = false) }
            } finally {
                _state.update { it.copy(isLoading = false, switchEdit = false) }
            }
        }
    }

    private fun getUser(){
        Log.d("ProfileViewModel", "getUser")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                fetchUserUseCase.invoke().collect { user ->
                    _state.update { it.copy(user = user, isLoading = false, editUser = user) }
                }
            } catch (e: Exception){
                Log.e("ProfileViewModel", "getUser: ${e.message}")
                _state.update { it.copy(isLoading = false) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

}

sealed class ProfileEvent{
    data class EditUser(val user: User): ProfileEvent()
    data object GetUser: ProfileEvent()
    data object SwitchEdit: ProfileEvent()
    data object UpdateUser: ProfileEvent()
}


data class ProfileState(
    var isLoading: Boolean = false,
    var switchEdit: Boolean = false,
    var user: User? = null,
    var editUser: User? = null
)