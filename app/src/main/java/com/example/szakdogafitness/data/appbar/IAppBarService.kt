package com.example.szakdogafitness.data.appbar

import androidx.compose.runtime.Composable

interface IAppBarService {

    fun appBarVisibility(screenRoute: String): Pair<Boolean, Boolean>

    fun appBarTitle(screenRoute: String): @Composable () -> Unit

    fun bottomBar(screenRoute: String, stateProvider: Any): @Composable () -> Unit

}