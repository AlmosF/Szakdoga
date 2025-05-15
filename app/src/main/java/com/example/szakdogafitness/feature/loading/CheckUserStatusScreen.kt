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
import com.example.szakdogafitness.feature.profile.ProfileEvent
import com.example.szakdogafitness.feature.profile.ProfileViewModel

@Composable
fun CheckUserStatusScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
    ) {
    val profileState = rememberUpdatedState(profileViewModel.state.collectAsStateWithLifecycle()).value

    LaunchedEffect(profileState.value.isLoading) {
        Log.d("ProfileViewModel", "Loading: ${profileState.value.isLoading}")
    }

    LaunchedEffect(Unit) {
        Log.d("ProfileViewModel", "ProfileState: ${profileState.value}")
        profileViewModel.onEvent(ProfileEvent.GetUser)
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