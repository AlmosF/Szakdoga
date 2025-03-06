package com.example.szakdogafitness.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.profile.AllProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: AllProfileUseCases
) : ViewModel(){

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()


    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.EditUser -> {

            }
            ProfileEvent.GetUser -> {
                viewModelScope.launch(Dispatchers.IO){
                    /*val user = profileUseCases.getUser()
                    Log.d("ProfileViewModel", "onEvent: $user")
                    _state.value = _state.value.copy(user = user)*/
                    profileUseCases.getUser().collect {
                        Log.d("ProfileViewModel", "onEvent: $it")
                        _state.value = _state.value.copy(user = it)
                    }
                }
            }
        }
    }
}

sealed class ProfileEvent{
    data class EditUser(val user: User): ProfileEvent()
    data object GetUser: ProfileEvent()
}


data class ProfileState(
    var user: User? = null
)