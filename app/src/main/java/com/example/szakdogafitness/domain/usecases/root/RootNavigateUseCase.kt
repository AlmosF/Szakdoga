package com.example.szakdogafitness.domain.usecases.root

import androidx.navigation.NavHostController
import com.example.szakdogafitness.data.root.IRootNavService

@Deprecated("Deprecated use case")
class RootNavigateUseCase(private val repository: IRootNavService){

    operator fun invoke(
        isLoggedIn: Boolean,
        isSetupDone: Boolean,
        isUserNull: Boolean,
        navHostController: NavHostController
    ) {
        repository.navigate(isLoggedIn, isSetupDone, isUserNull, navHostController)
    }

}