package com.example.szakdogafitness.data.root

import androidx.navigation.NavHostController
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import javax.inject.Inject

class RootNavService @Inject constructor(

) : IRootNavService {

    override fun navigate(
        isLoggedIn: Boolean,
        isSetupDone: Boolean,
        isUserNull: Boolean,
        navHostController: NavHostController
    ) {
        when(isLoggedIn){
            true -> {
                if (isSetupDone && !isUserNull){
                    navHostController.navigate(RootScreenRoute.MainScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                } else {
                    navHostController.navigate(RootScreenRoute.SetupScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
            false -> {
                navHostController.navigate(RootScreenRoute.Authentication.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
    }

}