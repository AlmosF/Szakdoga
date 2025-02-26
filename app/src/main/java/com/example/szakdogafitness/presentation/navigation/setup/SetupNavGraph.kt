package com.example.szakdogafitness.presentation.navigation.setup

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.szakdogafitness.feature.setup.SetupScreen
import com.example.szakdogafitness.feature.setup.trainee.TraineeOne
import com.example.szakdogafitness.feature.setup.trainee.TraineeTwo
import com.example.szakdogafitness.feature.setup.trainer.TrainerOne
import com.example.szakdogafitness.feature.setup.trainer.TrainerTwo
import com.example.szakdogafitness.viewmodel.SetupViewModel

@Composable
fun SetupNavGraph(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    navController: NavHostController,
    setupViewModel: SetupViewModel
    //VMs
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SetupScreenRoute.RoleSelection.route
    ){
        composable(SetupScreenRoute.RoleSelection.route){
            Log.d("SetupNavGraph", "Role:")
            SetupScreen(
                setupViewModel = setupViewModel
            )
        }
        composable(SetupScreenRoute.TrainerOne.route){
            Log.d("SetupNavGraph", "TrainerOne:")
            TrainerOne(
                setupViewModel = setupViewModel,
            )
        }
        composable(SetupScreenRoute.TrainerTwo.route){
            Log.d("SetupNavGraph", "TrainerTwo:")
            TrainerTwo(
                setupViewModel = setupViewModel,
            )
        }
        composable(SetupScreenRoute.TraineeOne.route){
            Log.d("SetupNavGraph", "TraineeOne:")
            TraineeOne(
                setupViewModel = setupViewModel,
            )
        }
        composable(SetupScreenRoute.TraineeTwo.route){
            Log.d("SetupNavGraph", "TraineeTwo:")
            TraineeTwo(
                setupViewModel = setupViewModel,
            )
        }
    }

}