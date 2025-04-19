package com.example.szakdogafitness.feature.loading

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel

@Composable
fun CheckUserStatusScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    //mainViewModel: MainViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
    ) {
    /*LaunchedEffect(Unit) { //With MainVM
        mainViewModel.onEvent(MainEvent.FetchUser)
        mainViewModel.onEvent(MainEvent.FetchDailyData)
        delay(1000)
        val isNewUser = mainViewModel.checkIfUserIsNew()
        val destination = if (isNewUser) RootScreenRoute.SetupScreen.route else RootScreenRoute.LifeStyle.route
        navController.navigate(destination) { popUpTo(0) }
    }*/
    val profileState = rememberUpdatedState(profileViewModel.state.collectAsStateWithLifecycle()).value

    LaunchedEffect(profileState.value.isLoading) {
        Log.d("ProfileViewModel", "Loading: ${profileState.value.isLoading}")
    }

    LaunchedEffect(Unit) {
        Log.d("ProfileViewModel", "ProfileState: ${profileState.value}")
        profileViewModel.onEvent(ProfileEvent.GetUser)
        //delay(1000)
        /*if(!profileState.value.isLoading){
            val isNewUser = profileViewModel.checkIfUserIsNewP()
            val destination = if (isNewUser) RootScreenRoute.SetupScreen.route else RootScreenRoute.LifeStyle.route
            navController.navigate(destination) { popUpTo(0) }
        }*/
    }
    LaunchedEffect(profileState.value.user != null, profileState.value.isLoading == false) {
        if(!profileState.value.isLoading && profileState.value.user != null){
            val isNewUser = profileViewModel.checkIfUserIsNewP()
            val destination = if (isNewUser) RootScreenRoute.SetupScreen.route else RootScreenRoute.LifeStyle.route
            navController.navigate(destination) { popUpTo(0) }
        }
    }
    Column (
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(400.dp)
        )
    }
}