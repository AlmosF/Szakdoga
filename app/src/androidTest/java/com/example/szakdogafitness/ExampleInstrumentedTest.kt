package com.example.szakdogafitness

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.szakdogafitness.data.auth.AuthService
import com.example.szakdogafitness.data.auth.FirebaseAuthService
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
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.szakdogafitness", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class AuthenticationViewModelIntegrationTest {

    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private val testEmail = "feketealmos0@gmail.com"
    private val testPassword = "Almos123"




    @Before
    fun setUp() {
        firebaseAuth = FirebaseAuth.getInstance()

        viewModel = AuthenticationViewModel(
            authentication = AllAuthenticationUseCases(
                signIn = SignInUseCase(FirebaseAuthService(firebaseAuth)),
                signUp = SignUpUseCase(FirebaseAuthService(firebaseAuth)),
                signOut = SignOutUseCase(FirebaseAuthService(firebaseAuth)),
                deleteAccount = DeleteAccountUseCase(FirebaseAuthService(firebaseAuth)),
                sendRecoveryEmail = SendRecoveryEmailUseCase(FirebaseAuthService(firebaseAuth)),
                currentUserId = CurrentUserIdUseCase(FirebaseAuthService(firebaseAuth)),
                currentUserEmail = CurrentUserEmailUseCase(FirebaseAuthService(firebaseAuth)),
                currentUser = CurrentUserUseCase(FirebaseAuthService(firebaseAuth)),
                hasUser = HasUserUseCase(FirebaseAuthService(firebaseAuth)),
                isOnline = IsOnlineUseCase(FirebaseAuthService(firebaseAuth)),
                repository = FirebaseAuthService(firebaseAuth)
            ),
            IsEmailValidUseCase(),
            PasswordsMatchUseCase()
        )
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onSignIn_validCredentials_userIdMatches() = runTest { //This is working
        val email = "feketealmos0@gmail.com"
        val password = "Almos123"
        val userId = "pHu5JaToGShaARnvDu35RrlEgj63"

        val firebaseAuth = FirebaseAuth.getInstance()
        val authService = FirebaseAuthService(
            firebaseAuth
        )

        authService.signIn(email, password)
        val userIdAfter = authService.currentUserId()
        assertNotNull(userIdAfter)
        assertEquals(userId, userIdAfter)
        firebaseAuth.signOut()
        assertNull(authService.currentUserId())
    }

    @Test
    fun testViewModelSignInAndGetUserId() {
        val latch = CountDownLatch(1)
        var userId: String? = null
        var success = false

        runBlocking {
            viewModel.onEvent(AuthenticationEvent.EmailChanged(testEmail))
            viewModel.onEvent(AuthenticationEvent.PasswordChanged(testPassword))
            viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)

            viewModel.uiEvent.collect { event ->
                Log.d("AuthenticationTest", "UiEvent: $event") // Add logging here
                when (event) {
                    is UiEvent.Success -> {
                        success = true
                        userId = viewModel.getUserId()
                        latch.countDown()
                    }
                    is UiEvent.Failure -> {
                        Log.e("AuthenticationTest", "UiEvent Failure: ${event.message}")
                        latch.countDown()
                    }
                }
            }
        }
        assertTrue(latch.await(10, TimeUnit.SECONDS))
        assertTrue(success)
        assertNotNull(userId)
        assertEquals("fvDumtRA50eL8UD4PWCWPGniOus1", userId)
        firebaseAuth.signOut()
    }
}