package com.example.szakdogafitness.feature.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.location.RequestLocationUpdatesUseCase
import com.example.szakdogafitness.domain.usecases.profile.AllProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val allProfileUseCases: AllProfileUseCases,
    private val location: RequestLocationUpdatesUseCase,
) : ViewModel(){

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private var locationJob: Job? = null


    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.EditUser -> {
                Log.d(
                    "ProfileViewModel",
                    "onEvent: EditUser ${event.user}"
                )
                _state.update { it.copy(editUser = event.user) }
                Log.d("ProfileViewModel", "onEvent: EditUser ${_state.value.editUser?.trainee?.height}")
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

            ProfileEvent.DropDownChanged -> {
                _state.update { it.copy(dropDownExpanded = !it.dropDownExpanded) }
            }

            ProfileEvent.ClearState -> {
                _state.update {
                    ProfileState()
                }
            }

            ProfileEvent.StartLocationMonitoring -> {
                startLocationMonitoring()
            }
            ProfileEvent.StopLocationMonitoring -> {
                stopLocationMonitoring()
            }

            is ProfileEvent.UploadProfilePicture -> {
                uploadProfilePic(event.uri)
            }
        }
    }
    private fun uploadProfilePic(uri: Uri){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allProfileUseCases.uploadProfilePicture(uri)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception){
                Log.e("ProfileViewModel", "uploadProfilePic: ${e.message}")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
    private fun stopLocationMonitoring(){
        locationJob?.cancel()
        locationJob = null
    }
    private fun startLocationMonitoring(){
        locationJob?.cancel()

        locationJob = viewModelScope.launch {
            location().collectLatest { pos ->
                _state.update {
                    it.copy(
                        user = it.editUser?.copy(
                            location = pos
                        )
                    )
                }
                Log.d("ProfileViewModel", "Location: $pos")
                updateUsersLocation()
            }
        }
    }

    private fun updateUsersLocation(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allProfileUseCases.updateUser(_state.value.user ?: return@launch)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception){
                Log.e("ProfileViewModel", "updateUsersLocation: ${e.message}")
                _state.update { it.copy(isLoading = false) }
            } finally {
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }



    private fun updateUser() {
        Log.d("ProfileViewModel", "updateUser")
        Log.d("ProfileViewModel", "updateUser Location: ${_state.value.editUser?.location}")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val user = _state.value.editUser ?: return@launch
                allProfileUseCases.updateUser(user)
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
                Log.d("ProfileViewModel", "getUser: ${_state.value.user}, loading: ${_state.value.isLoading}")
                allProfileUseCases.fetchUser.invoke().collect { user ->
                    _state.update { it.copy(user = user, isLoading = false, editUser = user) }
                    Log.d("ProfileViewModel-", "getUser: ${_state.value.user}, loading: ${_state.value.isLoading}")
                }
                Log.d("ProfileViewModel", "getUser: ${_state.value.user}, loading: ${_state.value.isLoading}")
            } catch (e: Exception){
                Log.e("ProfileViewModel", "getUser: ${e.message}")
                _state.update { it.copy(isLoading = false) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    /*fun checkIfUserIsNewP(): Boolean{
        return state.value.user == null
    }*/
    fun checkIfUserIsNewP(): Boolean{
        return state.value.user?.userId?.isBlank() ?: true
    }

}

sealed class ProfileEvent{
    data class EditUser(val user: User): ProfileEvent()
    data class UploadProfilePicture(val uri: Uri): ProfileEvent()
    data object GetUser: ProfileEvent()
    data object DropDownChanged: ProfileEvent()
    data object SwitchEdit: ProfileEvent()
    data object UpdateUser: ProfileEvent()
    data object ClearState: ProfileEvent()
    object StartLocationMonitoring: ProfileEvent()
    object StopLocationMonitoring: ProfileEvent()
}


data class ProfileState(
    var isLoading: Boolean = false,
    var switchEdit: Boolean = false,
    var dropDownExpanded: Boolean = false,
    var user: User? = null,
    var editUser: User? = null
)