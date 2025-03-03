package com.example.szakdogafitness.presentation.navigation.root

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.szakdogafitness.feature.auth.AuthenticateScreen
import com.example.szakdogafitness.presentation.navigation.main.MainScreen
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.SetupViewModel
import kotlinx.coroutines.launch

/*@Composable
fun RootNavigation(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
    authViewModel: AuthenticationViewModel,
    setupViewModel: SetupViewModel = viewModel()
){

    val authState by authViewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()
    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        authViewModel.checkSignInState()
        authViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    Log.d("RootNavigationEVENT", "Success")
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message?.asString(context) ?: ""
                        )
                    }
                }

                is UiEvent.Failure -> {

                    Log.d("RootNavigationEVENT", "Failure")
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(authState.isLoggedIn) {
        Log.d("RootNavigation", "isLoggedIn: ${authState.isLoggedIn}")
    }

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        when {
            !authState.isLoggedIn -> {
                AuthenticateScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = authViewModel,
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
}*/

@Composable
fun RootNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = viewModel()
){

    val state by authViewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(state.isLoggedIn) {
        when(state.isLoggedIn){
            true -> {
                navController.navigate(RootScreenRoute.MainScreen.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
            false -> {
                navController.navigate(RootScreenRoute.Authentication.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
    }

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

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            startDestination = RootScreenRoute.Authentication.route,
            navController = navController
        ) {
            composable(RootScreenRoute.Authentication.route) {
                AuthenticateScreen(
                    viewModel = authViewModel
                )
            }

            composable(RootScreenRoute.MainScreen.route){
                MainScreen(
                    authViewModel = authViewModel
                )
            }
        }

    }



}