package com.example.szakdogafitness.domain.usecases.appbar

import com.example.szakdogafitness.data.appbar.IAppBarService

class AppBarVisibilityUseCase (private val repository: IAppBarService){
    operator fun invoke(screenRoute: String) = repository.appBarVisibility(screenRoute)
}