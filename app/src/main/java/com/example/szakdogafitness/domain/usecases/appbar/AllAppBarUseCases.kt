package com.example.szakdogafitness.domain.usecases.appbar

import com.example.szakdogafitness.data.appbar.IAppBarService

class AllAppBarUseCases (
    val repository: IAppBarService,
    val appBarVisibilityUseCase: AppBarVisibilityUseCase
)