package com.example.szakdogafitness.domain.di.Authentication

import com.example.szakdogafitness.data.auth.AuthService
import com.example.szakdogafitness.domain.usecases.auth.AllAuthenticationUseCases
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserEmailUseCase
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserUseCase
import com.example.szakdogafitness.domain.usecases.auth.DeleteAccountUseCase
import com.example.szakdogafitness.domain.usecases.auth.HasUserUseCase
import com.example.szakdogafitness.domain.usecases.auth.SendRecoveryEmailUseCase
import com.example.szakdogafitness.domain.usecases.auth.SignInUseCase
import com.example.szakdogafitness.domain.usecases.auth.SignOutUseCase
import com.example.szakdogafitness.domain.usecases.auth.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllAuthenticationUseCasesModule {

    @Provides
    @Singleton
    fun provideCurrentUserIdUseCase(repository: AuthService): CurrentUserIdUseCase =
        CurrentUserIdUseCase(repository)

    @Provides
    @Singleton
    fun provideCurrentUserUseCase(repository: AuthService): CurrentUserUseCase =
        CurrentUserUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteAccountUseCase(repository: AuthService): DeleteAccountUseCase =
        DeleteAccountUseCase(repository)

    @Provides
    @Singleton
    fun provideHasUserUseCase(repository: AuthService): HasUserUseCase =
        HasUserUseCase(repository)

    @Provides
    @Singleton
    fun provideSendRecoveryEmailUseCase(repository: AuthService): SendRecoveryEmailUseCase =
        SendRecoveryEmailUseCase(repository)

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AuthService): SignInUseCase =
        SignInUseCase(repository)

    @Provides
    @Singleton
    fun provideSignOutUseCase(repository: AuthService): SignOutUseCase =
        SignOutUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AuthService): SignUpUseCase =
        SignUpUseCase(repository)

    @Provides
    @Singleton
    fun provideCurrentUserEmailUseCase(repository: AuthService): CurrentUserEmailUseCase =
        CurrentUserEmailUseCase(repository)

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(
        repository: AuthService,
        currentUserIdUseCase: CurrentUserIdUseCase,
        currentUserUseCase: CurrentUserUseCase,
        hasUserUseCase: HasUserUseCase,
        signUpUseCase: SignUpUseCase,
        signInUseCase: SignInUseCase,
        sendRecoveryEmailUseCase: SendRecoveryEmailUseCase,
        deleteAccountUseCase: DeleteAccountUseCase,
        signOutUseCase: SignOutUseCase,
        currentUserEmailUseCase: CurrentUserEmailUseCase
    ): AllAuthenticationUseCases = AllAuthenticationUseCases(
        repository,
        currentUserIdUseCase,
        currentUserUseCase,
        hasUserUseCase,
        signUpUseCase,
        signInUseCase,
        sendRecoveryEmailUseCase,
        deleteAccountUseCase,
        signOutUseCase,
        currentUserEmailUseCase
    )


}