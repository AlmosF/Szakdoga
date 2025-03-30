package com.example.szakdogafitness.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.szakdogafitness.domain.usecases.IsEmailValidUseCase
import com.example.szakdogafitness.ui.model.UiText
import com.example.szakdogafitness.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.szakdogafitness.R
import com.example.szakdogafitness.domain.usecases.PasswordsMatchUseCase
import com.example.szakdogafitness.domain.usecases.auth.AllAuthenticationUseCases
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import com.example.szakdogafitness.ui.model.toUiText

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authentication: AllAuthenticationUseCases,
    private val isEmailValid: IsEmailValidUseCase,
    private val passwordsMatch: PasswordsMatchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    val state = _state.asStateFlow()

    private val email get() = state.value.email

    private val password get() = state.value.password

    private val confirmPassword get() = state.value.confirmPassword

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AuthenticationEvent){
        when (event){
            is AuthenticationEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _state.update { it.copy(email = newEmail) }
            }
            is AuthenticationEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _state.update { it.copy(password = newPassword) }
            }
            is AuthenticationEvent.ConfirmPasswordChanged -> {
                val newConfirmPassword = event.password.trim()
                _state.update { it.copy(confirmPassword = newConfirmPassword) }
            }
            is AuthenticationEvent.NavigateToProfile -> {
                event.navGraph.navigate(RootScreenRoute.Authentication.route)
            }
            AuthenticationEvent.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }
            AuthenticationEvent.ConfirmPasswordVisibilityChanged -> {
                _state.update { it.copy(confirmPasswordVisibility = !state.value.confirmPasswordVisibility) }
            }
            AuthenticationEvent.AuthenticationButtonClicked -> {
                if (_state.value.isInRegisterMode)
                    onSignUp()
                else
                    onSignIn()
            }
            AuthenticationEvent.LogoutButtonClicked -> {
                if (_state.value.isLoggedIn){
                    Log.d("RootNavigation", "Logging out")
                    onLogOut()

                }
            }
            AuthenticationEvent.ChangeMode -> {
                _state.update { it.copy(isInRegisterMode = !state.value.isInRegisterMode) }
            }
            AuthenticationEvent.ForgotPasswordButtonClicked -> {
                onForgotPassword()
            }

        }
    }

    private fun onSignIn(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                if (!isEmailValid(email)){
                    _uiEvent.send(
                        UiEvent.Failure(
                            UiText.StringResource(R.string.email_invalid)
                        )
                    )
                } else {
                    if (password.isBlank()){
                        _uiEvent.send(
                            UiEvent.Failure(
                                UiText.StringResource(R.string.password_blank)
                            )
                        )
                    } else {
                        authentication.signIn(email, password)
                        _uiEvent.send(
                            UiEvent.Success(
                                UiText.StringResource(R.string.success_login)
                            )
                        )
                        checkSignInState()
                    }
                }
            } catch (e: Exception){
                _uiEvent.send(
                    UiEvent.Failure(
                        e.toUiText()
                    )
                )
            }
        }
    }

    private fun onSignUp(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                if (!isEmailValid(email)){
                    _uiEvent.send(
                        UiEvent.Failure(
                            UiText.StringResource(R.string.email_blank)
                        )
                    )
                } else {
                    if (!passwordsMatch(password, confirmPassword) && password.isNotBlank()) {
                        _uiEvent.send(
                            UiEvent.Failure(
                                UiText.StringResource(R.string.password_blank_n_dont_match)
                            )
                        )
                    } else {
                        authentication.signUp(email, password)
                        _uiEvent.send(
                            UiEvent.Success(
                                UiText.StringResource(R.string.success_registration)
                            )
                        )
                        onSignIn()
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(
                    UiEvent.Failure(
                        e.toUiText()
                    )
                )
            }
        }
    }
    private fun onLogOut(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                authentication.signOut()
                _uiEvent.send(
                    UiEvent.Success(
                        UiText.StringResource(R.string.success_logout)
                    )
                )
                checkSignInState()
            } catch (e: Exception) {
                _uiEvent.send(
                    UiEvent.Failure(
                        e.toUiText()
                    )
                )
            }
        }
    }
    private fun onForgotPassword(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                if(!isEmailValid(email)){
                    _uiEvent.send(
                        UiEvent.Failure(
                            UiText.StringResource(R.string.some_error_message)
                        )
                    )
                } else {
                    authentication.sendRecoveryEmail(email)
                    _uiEvent.send(
                        UiEvent.Success(
                            UiText.StringResource(R.string.success_recovery_email)
                        )
                    )
                }
            } catch (e: Exception) {
                _uiEvent.send(
                    UiEvent.Failure(
                        e.toUiText()
                    )
                )
            }
        }
    }

    fun checkSignInState() = _state.update { it.copy(isLoggedIn = authentication.hasUser() ) }

    fun checkOnlineState(context: Context) = _state.update {
        it.copy(
            isOnline = authentication.isOnline(context = context),
        )
    }

    fun getUserId(): String? = authentication.currentUserId()

}

data class AuthenticationState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisibility: Boolean = false,
    val confirmPasswordVisibility: Boolean = false,
    val isInRegisterMode: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isOnline: Boolean = false
)

sealed class AuthenticationEvent {
    data class EmailChanged(val email: String) : AuthenticationEvent()
    data class PasswordChanged(val password: String) : AuthenticationEvent()
    data class ConfirmPasswordChanged(val password: String) : AuthenticationEvent()
    data class NavigateToProfile(val navGraph: NavHostController): AuthenticationEvent()
    object PasswordVisibilityChanged : AuthenticationEvent()
    object ConfirmPasswordVisibilityChanged : AuthenticationEvent()
    object AuthenticationButtonClicked : AuthenticationEvent()
    object LogoutButtonClicked : AuthenticationEvent()
    object ChangeMode: AuthenticationEvent()
    object ForgotPasswordButtonClicked: AuthenticationEvent()
}