package com.example.szakdogafitness.domain.usecases.auth

import com.example.szakdogafitness.data.auth.AuthService

class AllAuthenticationUseCases(
    val repository: AuthService,
    val currentUserId: CurrentUserIdUseCase,
    val currentUser: CurrentUserUseCase,
    val hasUser: HasUserUseCase,
    val isOnline: IsOnlineUseCase,
    val signUp : SignUpUseCase,
    val signIn : SignInUseCase,
    val sendRecoveryEmail : SendRecoveryEmailUseCase,
    val deleteAccount : DeleteAccountUseCase,
    val signOut : SignOutUseCase,
    val currentUserEmail : CurrentUserEmailUseCase
)