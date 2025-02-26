package com.example.szakdogafitness.presentation.navigation.root

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.szakdogafitness.feature.auth.AuthenticateScreen
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.SetupViewModel

@Composable
fun RootNavigation(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
    authViewModel: AuthenticationViewModel,
    setupViewModel: SetupViewModel = viewModel()
){

    val authState by authViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(authState.isLoggedIn) {
        Log.d("RootNavigation", "isLoggedIn: ${authState.isLoggedIn}")
    }


    when {
        !authState.isLoggedIn -> {
            AuthenticateScreen(
                modifier = modifier,
                viewModel = authViewModel
            )
        }
        else -> {
            Text(
                "Logged in",
                modifier = Modifier
                    .clickable{
                        Log.d("RootNavigation", "Logout")
                        authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                    }
            )
        }
    }
}