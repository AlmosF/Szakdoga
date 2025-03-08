package com.example.szakdogafitness.presentation.navigation.root

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.szakdogafitness.feature.auth.AuthenticateScreen
import com.example.szakdogafitness.feature.setup.SetupScreenBox
import com.example.szakdogafitness.presentation.navigation.main.MainNavigation
import com.example.szakdogafitness.presentation.navigation.main.MainScreen
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.RootNavEvent
import com.example.szakdogafitness.viewmodel.RootNavViewModel
import kotlinx.coroutines.launch

@Composable
fun RootNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = viewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    rootNavViewModel: RootNavViewModel = hiltViewModel()
){

    val state by authViewModel.state.collectAsStateWithLifecycle()

    val profileState by profileViewModel.state.collectAsStateWithLifecycle()


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) { //NavHostController initialization
        rootNavViewModel.onEvent(RootNavEvent.NavController(navController))
    }

    LaunchedEffect(state.isLoggedIn) {
        when(state.isLoggedIn){ //Change it to If
            true -> {
                profileViewModel.onEvent(ProfileEvent.GetUser)
            }

            false -> {}
        }
    }

    /*LaunchedEffect(state.isLoggedIn) {
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
    }*/

    LaunchedEffect(state.isLoggedIn == true, profileState.user?.isSetupDone) {

        Log.d("RootNavigation-LE", "isLoggedIn: ${state.isLoggedIn}")
        Log.d("RootNavigation-LE", "isSetupDone: ${profileState.user?.isSetupDone}")
        Log.d("RootNavigation-LE", "state: ${profileState.user}")
        rootNavViewModel.onEvent(
            RootNavEvent.Navigate(
                isLoggedIn = state.isLoggedIn,
                isSetupDone = profileState.user?.isSetupDone == true,
                isProfileNull = profileState.user == null
            )
        )
        /*when(state.isLoggedIn){
            true -> {
                if (profileState.user?.isSetupDone == true && profileState.user?.isSetupDone != null){
                    navController.navigate(RootScreenRoute.MainScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                } else if (profileState.user?.isSetupDone == false || profileState.user?.isSetupDone == null) {

                    navController.navigate(RootScreenRoute.SetupScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
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
        }*/
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
                    viewModel = authViewModel,
                    navController = navController
                )
            }

            composable(RootScreenRoute.SetupScreen.route){
                SetupScreenBox(
                ){
                    //authViewModel.onEvent(AuthenticationEvent.NavigateToProfile(navGraph = navController))
                }
            }

            composable(RootScreenRoute.MainScreen.route){
                /*MainScreen(
                    authViewModel = authViewModel,
                    profileViewModel = profileViewModel
                )*/
                MainNavigation(
                    rootNavController = navController,
                    snackbarHostState = snackbarHostState,
                    authViewModel = authViewModel,
                    profileViewModel = profileViewModel
                )
                /*SetupScreenBox(){
                    authViewModel.onEvent(AuthenticationEvent.NavigateToProfile(navGraph = navController))
                }*/
            }
        }

    }
}