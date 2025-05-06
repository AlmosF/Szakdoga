package com.example.szakdogafitness.Profile

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
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup(){
        Dispatchers.setMain(dispatcher)

        val allProfileUseCases = AllProfileUseCases(
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




}