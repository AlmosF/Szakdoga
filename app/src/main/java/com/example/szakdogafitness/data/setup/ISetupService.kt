package com.example.szakdogafitness.data.setup

import androidx.navigation.NavHostController
import com.example.szakdogafitness.feature.setup.SetupProfileState

interface ISetupService {
    fun nextScreen(currentScreen: Int, role: Int, navHostController: NavHostController)
    fun previousScreen(currentScreen: Int, role: Int, navHostController: NavHostController)
    fun nextButtonEnabled(state: SetupProfileState) : Boolean
    fun previousButtonVisible(currentScreen: Int) : Boolean
    fun nextButtonLabel(currentScreen: Int): String
    suspend fun finishButtonClicked(state: SetupProfileState)
}