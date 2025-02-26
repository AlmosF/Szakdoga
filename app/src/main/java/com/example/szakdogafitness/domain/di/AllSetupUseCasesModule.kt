package com.example.szakdogafitness.domain.di

//import com.example.szakdogafitness.domain.usecases.setup.SetupButtonsUseCase
import com.example.szakdogafitness.data.setup.ISetupService
import com.example.szakdogafitness.domain.usecases.setup.AllSetupUseCases
import com.example.szakdogafitness.domain.usecases.setup.FinishButtonUseCase
import com.example.szakdogafitness.domain.usecases.setup.NextButtonEnabledUseCase
import com.example.szakdogafitness.domain.usecases.setup.NextButtonLabelUseCase
import com.example.szakdogafitness.domain.usecases.setup.NextScreenUseCase
import com.example.szakdogafitness.domain.usecases.setup.PreviousButtonVisibleUseCase
import com.example.szakdogafitness.domain.usecases.setup.PreviousScreenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllSetupUseCasesModule {
    @Provides
    @Singleton
    fun provideNextScreenUseCase(repository: ISetupService): NextScreenUseCase {
        return NextScreenUseCase(repository)
    }

    @Provides
    @Singleton
    fun providePreviousScreenUseCase(repository: ISetupService): PreviousScreenUseCase {
        return PreviousScreenUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideNextButtonEnabledUseCase(repository: ISetupService): NextButtonEnabledUseCase {
        return NextButtonEnabledUseCase(repository)
    }

    @Provides
    @Singleton
    fun providePreviousButtonVisibleUseCase(repository: ISetupService): PreviousButtonVisibleUseCase {
        return PreviousButtonVisibleUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideNextButtonLabelUseCase(repository: ISetupService): NextButtonLabelUseCase {
        return NextButtonLabelUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFinishButtonUseCase(repository: ISetupService): FinishButtonUseCase {
        return FinishButtonUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSetupUseCases(
        repository: ISetupService,
        nextScreen: NextScreenUseCase,
        previousScreen: PreviousScreenUseCase,
        nextButtonEnabled: NextButtonEnabledUseCase,
        previousButtonVisible: PreviousButtonVisibleUseCase,
        nextButtonLabel: NextButtonLabelUseCase,
        finishButtonClicked: FinishButtonUseCase
    ): AllSetupUseCases = AllSetupUseCases(
        repository = repository,
        nextScreen = nextScreen,
        previousScreen = previousScreen,
        nextButtonEnabled = nextButtonEnabled,
        previousButtonVisible = previousButtonVisible,
        nextButtonLabel = nextButtonLabel,
        finishButton = finishButtonClicked
    )
}