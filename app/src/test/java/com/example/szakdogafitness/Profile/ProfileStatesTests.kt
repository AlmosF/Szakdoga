package com.example.szakdogafitness.Profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.data.profile.IProfileService
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.location.RequestLocationUpdatesUseCase
import com.example.szakdogafitness.domain.usecases.profile.AllProfileUseCases
import com.example.szakdogafitness.domain.usecases.profile.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.GetProfileUseCase
import com.example.szakdogafitness.domain.usecases.profile.UpdateUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.UploadProfilePictureUseCase
import com.example.szakdogafitness.feature.profile.ProfileEvent
import com.example.szakdogafitness.feature.profile.ProfileState
import com.example.szakdogafitness.feature.profile.ProfileViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileStatesTests {


    private lateinit var viewModel: ProfileViewModel

    private val repository: IProfileService = mockk()
    private val getUser = mockk<GetProfileUseCase>()
    private val fetchUser = mockk<FetchUserUseCase>()
    private val updateUser = mockk<UpdateUserUseCase>()
    private val uploadProfilePicture = mockk<UploadProfilePictureUseCase>(relaxed = true)
    private val location = mockk<RequestLocationUpdatesUseCase>(relaxed = true)

    private lateinit var allProfileUseCases: AllProfileUseCases

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup(){
        Dispatchers.setMain(dispatcher)

        allProfileUseCases = AllProfileUseCases(
            repository,
            getUser,
            fetchUser,
            updateUser,
            uploadProfilePicture
        )

        viewModel = ProfileViewModel(
            allProfileUseCases,
            location
        )

    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun editUserTest() {
        val testUser = User(userId = "Unique123")
        viewModel.onEvent(ProfileEvent.EditUser(testUser))
        assertEquals(testUser, viewModel.state.value.editUser)
    }

    @Test
    fun switchEditTest(){
        val testUser = User(userId = "Unique123")
        viewModel.state.value.user = testUser
        viewModel.onEvent(ProfileEvent.SwitchEdit)
        assertEquals(testUser, viewModel.state.value.editUser)
        assertEquals(true, viewModel.state.value.switchEdit)
    }

    @Test
    fun dropDownTest(){
        viewModel.onEvent(ProfileEvent.DropDownChanged)
        assertEquals(true, viewModel.state.value.dropDownExpanded)
    }

    @Test
    fun clearState(){
        val testUser = User(userId = "Unique123")
        viewModel.onEvent(ProfileEvent.EditUser(testUser))
        assertNotEquals(ProfileState(), viewModel.state.value)
        viewModel.onEvent(ProfileEvent.ClearState)
        assertEquals(ProfileState(), viewModel.state.value)
    }

    @Test
    fun uploadProfilePictureTest() = runTest {
        val testUri = mockk<Uri>() // Mock a Uri object
        val initialState = viewModel.state.value

        // Mock the behavior of the uploadProfilePicture use case
        coEvery { allProfileUseCases.uploadProfilePicture(testUri) } returns ""

        // Trigger the event
        viewModel.onEvent(ProfileEvent.UploadProfilePicture(testUri))

        // Assert that the state is updated to loading
        //assertEquals(true, viewModel.state.value.isLoading)

        // Advance the coroutine to simulate the completion of the upload
        advanceUntilIdle()

        // Assert that the state is no longer loading
        assertEquals(false, viewModel.state.value.isLoading)

        // Verify that the use case was called with the correct parameter
        coVerify { allProfileUseCases.uploadProfilePicture(testUri) }

        // Ensure no unexpected state changes occurred
        assertEquals(initialState.copy(isLoading = false), viewModel.state.value)
    }

    @Test
    fun updateUserTest() = runTest {
        val testUser = User(userId = "Test123")
        viewModel.onEvent(ProfileEvent.EditUser(testUser))

        coEvery { allProfileUseCases.updateUser(testUser) } returns Unit

        viewModel.onEvent(ProfileEvent.UpdateUser)


        advanceUntilIdle()

        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(false, viewModel.state.value.switchEdit)

        coVerify { allProfileUseCases.updateUser(testUser) }
    }

    @Test
    fun getUserTest() = runTest {
        val testUser = User(userId = "Test123")
        coEvery { allProfileUseCases.fetchUser.invoke() } returns flowOf(testUser)

        viewModel.onEvent(ProfileEvent.GetUser)

        advanceUntilIdle()

        assertEquals(testUser, viewModel.state.value.user)
        assertEquals(testUser, viewModel.state.value.editUser)
        assertEquals(false, viewModel.state.value.isLoading)

        coVerify { allProfileUseCases.fetchUser.invoke() }
    }



}