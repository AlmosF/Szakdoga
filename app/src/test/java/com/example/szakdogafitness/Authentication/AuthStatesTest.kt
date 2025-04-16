package com.example.szakdogafitness.Authentication

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.szakdogafitness.data.auth.AuthService
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
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


@OptIn(ExperimentalCoroutinesApi::class)
class AuthStatesTest {

    private lateinit var viewModel: AuthenticationViewModel


    private val signInUseCase = mockk<SignInUseCase>()
    private val signUpUseCase = mockk<SignUpUseCase>()
    private val repository = mockk<AuthService>(relaxed = true)
    private val currentUserId = mockk<CurrentUserIdUseCase>(relaxed = true)
    private val currentUser = mockk<CurrentUserUseCase>(relaxed = true)
    private val hasUser = mockk<HasUserUseCase>(relaxed = true)
    private val isOnline = mockk<IsOnlineUseCase>(relaxed = true)
    private val sendRecoveryEmail = mockk<SendRecoveryEmailUseCase>(relaxed = true)
    private val deleteAccount = mockk<DeleteAccountUseCase>(relaxed = true)
    private val signOut = mockk<SignOutUseCase>()
    private val currentUserEmail = mockk<CurrentUserEmailUseCase>(relaxed = true)
    private val isEmailValid = mockk<IsEmailValidUseCase>()
    private val passwordsMatch = mockk<PasswordsMatchUseCase>()

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

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun onEventEmailChanged() {
        val newEmail = "feketealmos0@gmail.com"
        viewModel.onEvent(AuthenticationEvent.EmailChanged(newEmail))
        assertEquals(
            viewModel.state.value.email,
            newEmail
        )
    }

    @Test
    fun onEventPasswordChanged() {
        val newPassword = "Almos123"
        viewModel.onEvent(AuthenticationEvent.PasswordChanged(newPassword))
        assertEquals(
            viewModel.state.value.password,
            newPassword
        )
    }

    @Test
    fun onEventConfirmPasswordChanged() {
        val newWrongConfirmPassword = "Almos1234"
        viewModel.onEvent(AuthenticationEvent.ConfirmPasswordChanged(newWrongConfirmPassword))
        assertEquals(
            viewModel.state.value.confirmPassword,
            newWrongConfirmPassword
        )
    }

    @Test
    fun onEventAssertConfirmPasswordAndPasswordAreNotEquals() {
        val newPassword = "Almos123"
        val newWrongConfirmPassword = "Almos1234"
        viewModel.onEvent(AuthenticationEvent.PasswordChanged(newPassword))
        viewModel.onEvent(AuthenticationEvent.ConfirmPasswordChanged(newWrongConfirmPassword))
        assertNotEquals(
            viewModel.state.value.password,
            viewModel.state.value.confirmPassword
        )
    }

    @Test
    fun onEventAssertModeChange() {
        val initialMode = viewModel.state.value.isInRegisterMode
        viewModel.onEvent(AuthenticationEvent.ChangeMode)
        assertEquals(
            !initialMode,
            viewModel.state.value.isInRegisterMode
        )
    }

    @Test
    fun onEventAssertPasswordVisible() {
        val initialPasswordVisibility = viewModel.state.value.passwordVisibility
        viewModel.onEvent(AuthenticationEvent.PasswordVisibilityChanged)
        assertEquals(
            !initialPasswordVisibility,
            viewModel.state.value.passwordVisibility
        )
    }

    @Test
    fun onEventAssertConfirmPasswordVisible() {
        val initialConfirmPasswordVisibility = viewModel.state.value.confirmPasswordVisibility
        viewModel.onEvent(AuthenticationEvent.ConfirmPasswordVisibilityChanged)
        assertEquals(
            !initialConfirmPasswordVisibility,
            viewModel.state.value.confirmPasswordVisibility
        )
    }


    @Test
    fun AuthenticationButtonClickedWithMatchingPasswordTriggeresSuccessEvent() = runTest {
        val email = "feketealmosTest0@gmail.com"
        val password = "Password123"
        val confirmPassword = "Password123"

        every { isEmailValid(email) } returns true
        every { passwordsMatch(password, confirmPassword) } returns true

        coEvery { signUpUseCase(email, password) } just Runs

        viewModel.onEvent(AuthenticationEvent.ChangeMode)

        viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
        viewModel.onEvent(AuthenticationEvent.PasswordChanged(password))
        viewModel.onEvent(AuthenticationEvent.ConfirmPasswordChanged(confirmPassword))
        viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)
        val event = withTimeout(10000) {
            viewModel.uiEvent.first()
        }
        assertTrue(event is UiEvent.Success)
    }

    @Test
    fun AuthenticationButtonClickedWithInvalidEmailAddressTriggersFailureEvent() = runTest {
        val email = "feketealmos"
        val password = "Password123"
        val confirmPassword = "Password123"

        every { isEmailValid(email) } returns false
        every { passwordsMatch(password, confirmPassword) } returns true
        viewModel.onEvent(AuthenticationEvent.ChangeMode)
        viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
        viewModel.onEvent(AuthenticationEvent.PasswordChanged(password))
        viewModel.onEvent(AuthenticationEvent.ConfirmPasswordChanged(confirmPassword))
        viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)
        val event = withTimeout(5000) {
            viewModel.uiEvent.first()
        }
        assertTrue(event is UiEvent.Failure)

    }

    @Test
    fun AuthenticationButtonClickedWithMismatchedPasswordsTriggersFailureEvent() = runTest {
        val email = "feketealmosTest0@gmail.com"
        val password = "Password123"
        val confirmPassword = "NotMatching123"

        every { isEmailValid(email) } returns true
        every { passwordsMatch(password, confirmPassword) } returns false

        viewModel.onEvent(AuthenticationEvent.ChangeMode)

        viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
        viewModel.onEvent(AuthenticationEvent.PasswordChanged(password))
        viewModel.onEvent(AuthenticationEvent.ConfirmPasswordChanged(confirmPassword))
        viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)

        val event = withTimeout(5000) {
            viewModel.uiEvent.first()
        }
        assertTrue(event is UiEvent.Failure)
    }

    @Test
    fun AuthenticationButtonClickedWithSignInModeWithInvalidEmailTriggeresFailureEvent() = runTest {
        val email = "feketealmos"
        val password = "Password123"

        every { isEmailValid(email) } returns false
        every { passwordsMatch(password, password) } returns true

        viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
        viewModel.onEvent(AuthenticationEvent.PasswordChanged(password))
        viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)
        val event = withTimeout(5000) {
            viewModel.uiEvent.first()
        }
        assertTrue(event is UiEvent.Failure)
    }

    @Test
    fun AuthenticationButtonClickedWithSignInModeWithValidEmailTriggeresSuccessEvent() = runTest {
        val email = "feketealmos0@gmail.com"
        val password = "Almos123"

        every { isEmailValid(email) } returns true
        every { passwordsMatch(password, password) } returns true
        coEvery { signInUseCase(email, password) } just Runs
        viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
        viewModel.onEvent(AuthenticationEvent.PasswordChanged(password))
        viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)
        val event = withTimeout(5000) {
            viewModel.uiEvent.first()
        }
        assertTrue(event is UiEvent.Success)

    }

    @Test
    fun AuthenticationButtonClickedWithSignInModeWithBlankPasswordTriggeresFailureEvent() =
        runTest {
            val email = "feketealmos0@gmail.com"
            val password = ""

            every { isEmailValid(email) } returns true
            every { passwordsMatch(password, password) } returns true
            coEvery { signInUseCase(email, password) } just Runs
            viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
            viewModel.onEvent(AuthenticationEvent.PasswordChanged(password))
            viewModel.onEvent(AuthenticationEvent.AuthenticationButtonClicked)
            val event = withTimeout(5000) {
                viewModel.uiEvent.first()
            }
            assertTrue(event is UiEvent.Failure)
        }

    @Test
    fun forgotPasswordButtonClickedWithInvalidEmailAddressTriggersFailureEvent() = runTest {
        val email = "feketealmos"
        every { isEmailValid(email) } returns false

        viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
        viewModel.onEvent(AuthenticationEvent.ForgotPasswordButtonClicked)
        val event = withTimeout(5000) {
            viewModel.uiEvent.first()
        }
        assertTrue(event is UiEvent.Failure)
    }

    @Test
    fun forgotPasswordButtonClickedWithValidEmailAddressTriggersSuccessEvent() = runTest {
        val email = "feketealmos0@gmail.com"
        every { isEmailValid(email) } returns true
        coEvery { sendRecoveryEmail(email) } just Runs
        viewModel.onEvent(AuthenticationEvent.EmailChanged(email))
        viewModel.onEvent(AuthenticationEvent.ForgotPasswordButtonClicked)
        val event = withTimeout(5000) {
            viewModel.uiEvent.first()
        }
        assertTrue(event is UiEvent.Success)
    }

}