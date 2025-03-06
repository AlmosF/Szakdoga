package com.example.szakdogafitness.data.root

import androidx.navigation.NavHostController

interface IRootNavService{
    fun navigate(
        isLoggedIn: Boolean,
        isSetupDone: Boolean,
        isUserNull: Boolean,
        navHostController: NavHostController
    )
}