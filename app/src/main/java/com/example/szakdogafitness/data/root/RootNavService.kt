package com.example.szakdogafitness.data.root

import android.util.Log
import androidx.navigation.NavHostController
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import javax.inject.Inject

@Deprecated("Deprecated class")
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
                    Log.d("RootNavigation_:", "LifeStyle")
                    //navHostController.navigate(RootScreenRoute.MainScreen.route) {
                    navHostController.navigate(RootScreenRoute.LifeStyle.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                } else {
                    Log.d("RootNavigation_:", "Setup")
                    Log.d("RootNavigation_:", "isSetupDone: $isSetupDone")
                    Log.d("RootNavigation_:", "isUserNull: $isUserNull")
                    navHostController.navigate(RootScreenRoute.SetupScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
            false -> {
                Log.d("RootNavigation_:", "AUTH")
                navHostController.navigate(RootScreenRoute.Authentication.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
    }

}