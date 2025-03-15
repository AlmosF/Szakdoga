package com.example.szakdogafitness.presentation.navigation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
//import com.example.szakdogafitness.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.szakdogafitness.viewmodel.ProfileEvent

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    //profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val authState by authViewModel.state.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Main Screen - Composable"
        )

        /*if (profileViewModel.state.value.user!!.isSetupDone){
            Text(
                "Setup is done",
                modifier = Modifier
                    .clickable(
                        onClick = {
                            authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                        }
                    )
            )
        } else {
            Text(
                "Setup is not done"
            )
        }*/

    }


    /*val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = SnackbarHostState()
    LaunchedEffect(Unit) {
        authViewModel.checkSignInState()
        authViewModel.uiEvent.collect { event ->
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
    Column (
        modifier = modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
            }
        ) {
            Text("LogOut")
        }
    }*/
}