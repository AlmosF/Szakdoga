package com.example.szakdogafitness.data.appbar

import androidx.compose.runtime.Composable
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import javax.inject.Inject

class AppBarService @Inject constructor(

) : IAppBarService {


    override fun appBarVisibility(screenRoute: String): Pair<Boolean, Boolean> {

        when(screenRoute) {
            RootScreenRoute.Authentication.route -> {
                return Pair(false, false)
            }
            RootScreenRoute.SetupScreen.route -> {
                return Pair(false, false)
            }
            RootScreenRoute.LifeStyle.route -> {
                //return Pair(false, true)
                return Pair(true, true)
            }
            RootScreenRoute.Nutrition.route -> {
                return Pair(true, true)
            }
            RootScreenRoute.Search.route -> {
                return Pair(true, true)
            }
            RootScreenRoute.Trainees.route -> {
                return Pair(true, true)
            }
            RootScreenRoute.Trainer.route -> {
                return Pair(true, true)
            }
            RootScreenRoute.Requests.route -> {
                return Pair(true, true)
            }
            RootScreenRoute.Menu.route -> {
                return Pair(true, true)
            }
            else -> {
                return Pair(true, true)
            }
        }

    }


    override fun bottomBar(
        screenRoute: String,
        stateProvider: Any
    ): @Composable (() -> Unit) {
        TODO("Not yet implemented")
    }


    override fun appBarTitle(screenRoute: String): @Composable (() -> Unit) {
        when(screenRoute) {
            RootScreenRoute.Authentication.route -> {
                return {}
            }
            RootScreenRoute.SetupScreen.route -> {
                return {}
            }
            RootScreenRoute.LifeStyle.route -> {
                return {

                }
            }
            RootScreenRoute.Nutrition.route -> {
                return { }
            }
            RootScreenRoute.Search.route -> {
                return { }
            }
            RootScreenRoute.Trainees.route -> {
                return { }
            }
            RootScreenRoute.Trainer.route -> {
                return { }
            }
            RootScreenRoute.Requests.route -> {
                return { }
            }
            RootScreenRoute.Menu.route -> {
                return { }
            }
            else -> {
                return { }
            }
        }
    }



}