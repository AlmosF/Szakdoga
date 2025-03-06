package com.example.szakdogafitness.domain.usecases.profile

import com.example.szakdogafitness.data.profile.IProfileService

class AllProfileUseCases (
    val repository: IProfileService,
    val getUser: GetProfileUseCase,
)