package com.example.szakdogafitness.domain.usecases.setup

import com.example.szakdogafitness.data.setup.ISetupService

class AllSetupUseCases(
    val repository: ISetupService,
    val nextScreen: NextScreenUseCase,
    val previousScreen: PreviousScreenUseCase,
    val nextButtonEnabled: NextButtonEnabledUseCase,
    val previousButtonVisible: PreviousButtonVisibleUseCase,
    val nextButtonLabel: NextButtonLabelUseCase,
    val finishButton: FinishButtonUseCase
)