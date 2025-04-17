package com.example.szakdogafitness.Authentication

import com.example.szakdogafitness.data.auth.AuthService
import com.example.szakdogafitness.data.auth.FirebaseAuthService
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.IsEmailValidUseCase
import com.example.szakdogafitness.domain.usecases.PasswordsMatchUseCase
import com.example.szakdogafitness.domain.usecases.auth.AllAuthenticationUseCases
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserEmailUseCase
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserUseCase
import com.example.szakdogafitness.domain.usecases.auth.DeleteAccountUseCase
import com.example.szakdogafitness.domain.usecases.auth.HasUserUseCase
import com.example.szakdogafitness.domain.usecases.auth.IsOnlineUseCase
import com.example.szakdogafitness.domain.usecases.auth.SendRecoveryEmailUseCase
import com.example.szakdogafitness.domain.usecases.auth.SignInUseCase
import com.example.szakdogafitness.domain.usecases.auth.SignOutUseCase
import com.example.szakdogafitness.domain.usecases.auth.SignUpUseCase
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// WORKING
@OptIn(ExperimentalCoroutinesApi::class)
class AuthServiceTest {


    private lateinit var viewModel: AuthenticationViewModel

    private val signInUseCase = mockk<SignInUseCase>()
    private val signUpUseCase = mockk<SignUpUseCase>()
    private val repository = mockk<AuthService>(relaxed = true)
    private val repository2 = mockk<FirebaseAuthService>(relaxed = true)
    private val currentUserId = mockk<CurrentUserIdUseCase>(relaxed = true)
    private val currentUser = mockk<CurrentUserUseCase>(relaxed = true)
    private val hasUser = mockk<HasUserUseCase>(relaxed = true)
    private val isOnline = mockk<IsOnlineUseCase>(relaxed = true)
    private val sendRecoveryEmail = mockk<SendRecoveryEmailUseCase>(relaxed = true)
    private val deleteAccount = mockk<DeleteAccountUseCase>(relaxed = true)
    private val signOut = mockk<SignOutUseCase>(relaxed = true)
    private val currentUserEmail = mockk<CurrentUserEmailUseCase>(relaxed = true)
    private val isEmailValid = mockk<IsEmailValidUseCase>()
    private val passwordsMatch = mockk<PasswordsMatchUseCase>()

    val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseAS = FirebaseAuthService(firebaseAuth)

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        val allAuthUseCases = AllAuthenticationUseCases(
            repository = repository,
            currentUserId = currentUserId,
            currentUser = currentUser,
            hasUser = hasUser,
            isOnline = isOnline,
            signUp = signUpUseCase,
            signIn = signInUseCase,
            sendRecoveryEmail = sendRecoveryEmail,
            deleteAccount = deleteAccount,
            signOut = signOut,
            currentUserEmail = currentUserEmail
        )

        viewModel = AuthenticationViewModel(
            authentication = allAuthUseCases,
            isEmailValid = isEmailValid,
            passwordsMatch = passwordsMatch
        )
    }



    @Test
    fun isCurrentUserIdWorking() = runTest {
        every { firebaseAuth.currentUser?.uid } returns "7QPyGPyAeISdfwXe0teOeWMPwVz2"
        val userId = firebaseAS.currentUserId()
        assertNotNull(userId)
        assertEquals(
            "7QPyGPyAeISdfwXe0teOeWMPwVz2",
            userId
        )
    }

    @Test
    fun isLoggedIfCurrentUserNotNull() = runTest {
        every { firebaseAuth.currentUser } returns mockk()
        val hasUser = firebaseAS.hasUser()
        assertTrue(hasUser)
    }

    @Test
    fun isLoggedIfCurrentUserIsNull() = runTest {
        every { firebaseAuth.currentUser } returns null
        val hasUser = firebaseAS.hasUser()
        assertFalse(hasUser)
    }

    @Test
    fun currentUser() = runTest {
        every {firebaseAuth.currentUser?.uid } returns "7QPyGPyAeISdfwXe0teOeWMPwVz2"
        val currentUser = firebaseAS.currentUser()
        assertNotNull(currentUser)
        assertEquals(
            User(
                userId = "7QPyGPyAeISdfwXe0teOeWMPwVz2"
            ),
            currentUser
        )
    }

    @Test
    fun currentUserEmail() = runTest {
        every { firebaseAuth.currentUser?.email } returns "feketealmos0@gmail.com"
        val currentUserEmail = firebaseAS.currentUserEmail()
        assertNotNull(currentUserEmail)
        assertEquals(
            "feketealmos0@gmail.com",
            currentUserEmail
        )
    }

}