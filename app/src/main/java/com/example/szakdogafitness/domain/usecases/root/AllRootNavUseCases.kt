package com.example.szakdogafitness.domain.usecases.root

import com.example.szakdogafitness.data.root.IRootNavService

class AllRootNavUseCases(
    val repository: IRootNavService,
    val rootNavigate: RootNavigateUseCase
)