package com.example.szakdogafitness.data.appbar

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import javax.inject.Inject

class AppBarService @Inject constructor(

) : IAppBarService {


    override fun appBarVisibility(screenRoute: String): Pair<Boolean, Boolean> {

        when(screenRoute) {
            RootScreenRoute.Authentication.route -> {
                Log.d("RootNavigation-AppBar", "Auth")
                return Pair(false, false)
            }
            RootScreenRoute.SetupScreen.route -> {
                Log.d("RootNavigation-AppBar", "Setup")
                return Pair(false, false)
            }
            RootScreenRoute.LifeStyle.route -> {
                Log.d("RootNavigation-AppBar", "Lifestlye")
                //return Pair(false, true)
                return Pair(true, true)
            }
            RootScreenRoute.Nutrition.route -> {
                Log.d("RootNavigation-AppBar", "Nutrition")
                return Pair(true, true)
            }
            RootScreenRoute.Search.route -> {
                Log.d("RootNavigation-AppBar", "Search")
                return Pair(true, true)
            }
            RootScreenRoute.Trainees.route -> {
                Log.d("RootNavigation-AppBar", "Trainee")
                return Pair(true, true)
            }
            RootScreenRoute.Trainer.route -> {
                Log.d("RootNavigation-AppBar", "Trainer")
                return Pair(true, true)
            }
            RootScreenRoute.Requests.route -> {
                Log.d("RootNavigation-AppBar", "Req")
                return Pair(true, true)
            }
            RootScreenRoute.Menu.route -> {
                Log.d("RootNavigation-AppBar", "Menu")
                return Pair(true, true)
            }
            RootScreenRoute.NutritionSearch.route ->{
                Log.d("RootNavigation-AppBar", "NutritionSearch")
                return Pair(true, true)
            }
            RootScreenRoute.SearchDetail.route -> {
                Log.d("RootNavigation-AppBar", "SearchDetail")
                return Pair(true, true)
            }
            RootScreenRoute.Profile.route -> {
                Log.d("RootNavigation-AppBar", "Profile")
                return Pair(true, false)
            }
            RootScreenRoute.EditFoods.route -> {
                Log.d("RootNavigation-AppBar", "EditFoods")
                return Pair(true, false)
            }
            RootScreenRoute.EditFoodDetail.route -> {
                Log.d("RootNavigation-AppBar", "EditFoodDetail")
                return Pair(true, false)
            }
            RootScreenRoute.TraineeDetail.route -> {
                Log.d("RootNavigation-AppBar", "TraineeDetail")
                return Pair(true, false)
            }
            else -> {
                Log.d("RootNavigation-AppBar", "else")
                //return Pair(true, true)
                return Pair(false, false)
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