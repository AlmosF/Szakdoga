package com.example.szakdogafitness.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.szakdogafitness.ui.common.EmailTextField
import com.example.szakdogafitness.ui.common.PasswordTextField
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.launch

@Composable
fun AuthenticateScreen(
    modifier : Modifier = Modifier,
    viewModel: AuthenticationViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.checkSignInState()
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message?.asString(context) ?: ""
                        )
                    }
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) { padding ->
        if (state.isLoggedIn){
            OutlinedButton(
                onClick = {
                    viewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                }
            ) {
                Text(
                    text = "Logout"
                )
            }
        } else {
            Column (
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailTextField(
                    value = state.email,
                    label = "Email",
                    onValueChange = { //email = it
                        viewModel.onEvent(AuthenticationEvent.EmailChanged(it))
                    },
                    onDone = {},
                    imeAction = ImeAction.Next,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .width(64.dp)
                )
                PasswordTextField(
                    value = state.password,
                    label = "Password",
                    onValueChange = {
                        viewModel.onEvent(AuthenticationEvent.PasswordChanged(it))
                    },
                    onDone = {},
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(TextFieldDefaults.MinWidth)
                        .height(64.dp),
                    isVisible = state.passwordVisibility,
                    onVisibilityChanged = {
                        viewModel.onEvent(AuthenticationEvent.PasswordVisibilityChanged)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = null
                        )
                    }
                )
                AnimatedVisibility(visible = state.isInRegisterMode) {
                    PasswordTextField(
                        value = state.confirmPassword,
                        label = "Password again",
                        onValueChange = {
                            viewModel.onEvent(AuthenticationEvent.ConfirmPasswordChanged(it))
                        },
                        onDone = {},
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(TextFieldDefaults.MinWidth)
                            .height(64.dp),
                        isVisible = state.confirmPasswordVisibility,
                        onVisibilityChanged = {
                            viewModel.onEvent(AuthenticationEvent.ConfirmPasswordVisibilityChanged)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Password,
                                contentDescription = null
                            )
                        }
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                ) {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(AuthenticationEvent.ForgotPasswordButtonClicked)
                        }
                    ) {
                        Text(
                            text = "Forgot password?"
                        )
                    }
                }
                Button(
                    onClick = {
                        viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)
                    }
                ) {
                    Text(
                        text = if (state.isInRegisterMode) "Register" else "Login"
                    )
                }
                Text(
                    text = if (state.isInRegisterMode) "Already have an account?" else "Don't have an account?",
                )
                TextButton(
                    onClick = {
                        viewModel.onEvent(AuthenticationEvent.ChangeMode)
                    }
                ) {
                    Text(
                        text = if (state.isInRegisterMode) "Login" else "Register"
                    )
                }
            }
        }
    }
}