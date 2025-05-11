package com.example.szakdogafitness.Profile

import com.example.szakdogafitness.data.profile.IProfileService
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.location.RequestLocationUpdatesUseCase
import com.example.szakdogafitness.domain.usecases.profile.AllProfileUseCases
import com.example.szakdogafitness.domain.usecases.profile.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.GetProfileUseCase
import com.example.szakdogafitness.domain.usecases.profile.UpdateUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.UploadProfilePictureUseCase
import com.example.szakdogafitness.feature.profile.ProfileEvent
import com.example.szakdogafitness.feature.profile.ProfileViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileServicesTests {
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

    @Test
    fun updateProfileTests() = runTest {

        val testUser = User(userId = "Test123")
        coEvery { allProfileUseCases.fetchUser.invoke() } returns flowOf(testUser)

        viewModel.onEvent(ProfileEvent.GetUser)

        advanceUntilIdle()
        coEvery { updateUser.invoke(testUser.copy(userId = "Edited123")) } just Runs
        viewModel.onEvent(ProfileEvent.EditUser(testUser.copy(userId = "Edited123")))
        advanceUntilIdle()
        viewModel.onEvent(ProfileEvent.UpdateUser)
        advanceUntilIdle()
        coVerify(exactly = 1) { updateUser.invoke(testUser.copy(userId = "Edited123")) }



    }

}