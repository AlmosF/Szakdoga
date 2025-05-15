package com.example.szakdogafitness.Trainers

import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.data.trainees.ITraineesService
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.trainers.AllTrainersUseCases
import com.example.szakdogafitness.domain.usecases.trainers.ApplyToTrainerAppointmentUseCase
import com.example.szakdogafitness.domain.usecases.trainers.FetchRequestedAppoint
import com.example.szakdogafitness.domain.usecases.trainers.FetchTrainersRequestsUseCase
import com.example.szakdogafitness.domain.usecases.trainers.GetAcceptedTrainersUseCase
import com.example.szakdogafitness.domain.usecases.trainers.GetTrainersUseCase
import com.example.szakdogafitness.domain.usecases.trainers.LoadAllTrainersAllRateUseCase
import com.example.szakdogafitness.domain.usecases.trainers.LoadTrainersRateUseCase
import com.example.szakdogafitness.domain.usecases.trainers.RateTrainerUseCase
import com.example.szakdogafitness.feature.trainer.TrainersEvent
import com.example.szakdogafitness.feature.trainer.TrainersViewModel
import com.google.firebase.Timestamp
import io.mockk.coEvery
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
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TrainersStatesTest {

    private lateinit var viewModel: TrainersViewModel

    private val repository: ITraineesService = mockk()
    private val applyToTrainerAppointment = mockk<ApplyToTrainerAppointmentUseCase>()
    private val fetchRequestedAppointments = mockk<FetchRequestedAppoint>()
    private val fetchTrainersRequests = mockk<FetchTrainersRequestsUseCase>()
    private val getAcceptedTrainers = mockk<GetAcceptedTrainersUseCase>()
    private val getTrainers = mockk<GetTrainersUseCase>()
    private val loadTrainersRate = mockk<LoadTrainersRateUseCase>()
    private val loadAllTrainersAllRate = mockk<LoadAllTrainersAllRateUseCase>()
    private val rateTrainer = mockk<RateTrainerUseCase>()

    private val dispatchers = StandardTestDispatcher()

    @Before
    fun setup(){
        Dispatchers.setMain(dispatchers)
        val allTrainersUseCases = AllTrainersUseCases(
            repository,
            applyToTrainerAppointment,
            fetchRequestedAppointments,
            fetchTrainersRequests,
            getAcceptedTrainers,
            getTrainers,
            loadTrainersRate,
            loadAllTrainersAllRate,
            rateTrainer
        )

        viewModel = TrainersViewModel(
            allTrainersUseCases,
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun filterTrainersTest() {
        viewModel.onEvent(TrainersEvent.FilterTrainers(true, false))
        assertEquals(true, viewModel.filter.value.filterMen)
        viewModel.onEvent(TrainersEvent.FilterTrainers(false, true))
        assertEquals(true, viewModel.filter.value.filterWomen)
        viewModel.onEvent(TrainersEvent.FilterTrainers(true, true))
        assertEquals(true, viewModel.filter.value.filterMen)
        assertEquals(true, viewModel.filter.value.filterWomen)
    }

    @Test
    fun trainerSelectedTest(){
        val dummyUser = User(
            trainer = Trainer(
                certifications = listOf(1,2),
                areasOfExperties = listOf(3,4),
            )
        )
        viewModel.onEvent(TrainersEvent.TrainerSelected(dummyUser))
        assertEquals(dummyUser, viewModel.state.value.selectedTrainer)
    }

    @Test
    fun loadAllTrainersStartAndAcceptedTrainersAssertEqualsTest() = runTest {
        val testList = listOf(
            Pair(Requests(traineeId = "1", status = "pending"), User(userId = "1")),
            Pair(Requests(traineeId = "2", status = "pending"), User(userId = "2")),
            Pair(Requests(traineeId = "3", status = "pending"), User(userId = "3")),
        )
        val testRateList = listOf(
            Pair("1", listOf(Rating(rating = 2))),
            Pair("2", listOf(Rating(rating = 3))),
            Pair("3", listOf(Rating(rating = 4))),
        )
        coEvery { getAcceptedTrainers.invoke() } returns flowOf(testList)
        coEvery { loadAllTrainersAllRate.invoke() } returns flowOf(testRateList)
        viewModel.onEvent(TrainersEvent.LoadAllTrainersAllRating)
        advanceUntilIdle()
        viewModel.onEvent(TrainersEvent.LoadAcceptedTrainers)
        advanceUntilIdle()
        assertEquals(testList, viewModel.state.value.acceptedTrainers)
    }

    @Test
    fun loadRequestedAppointAssertEqualTests() = runTest {
        val time = Timestamp(1747234747, 217000000)
        val testList = Pair(Requests(traineeId = "1", status = "pending", createdAt = time), User(userId = "1"))

        coEvery { fetchRequestedAppointments.invoke("dummyId") } returns flowOf(testList)
        viewModel.onEvent(TrainersEvent.FetchRequestedAppoint("dummyId"))
        advanceUntilIdle()
        assertEquals(testList, viewModel.state.value.requestedAppoint)
    }

    @Test
    fun loadTrainersRateAssertEquals() = runTest {
        val time = Timestamp(1747234747, 217000000)
        val testList = Rating(rating = 2, timestamp = time)

        coEvery { loadTrainersRate.invoke("dummyId") } returns flowOf(testList)
        viewModel.onEvent(TrainersEvent.LoadTrainersRate("dummyId"))
        advanceUntilIdle()
        assertEquals(testList, viewModel.state.value.trainersRate)
    }

    @Test
    fun fetchTrainersRequestAssertEqualsTests() = runTest {
        val testList = mapOf(
            "1" to listOf(1, 2, 3),
            "2" to listOf(4, 5, 6)
        )
        coEvery { fetchTrainersRequests.invoke("dummyId") } returns flowOf(testList)
        viewModel.onEvent(TrainersEvent.FetchTrainersRequests("dummyId"))
        advanceUntilIdle()
        assertEquals(testList, viewModel.state.value.trainersRequests)
    }

    @Test
    fun loadTrainersAssertEqualsTests() = runTest {
        val testList = listOf(
            User(userId = "1"),
            User(userId = "2"),
            User(userId = "3")
        )
        coEvery { getTrainers.invoke() } returns flowOf(testList)
        viewModel.onEvent(TrainersEvent.LoadTrainers)
        advanceUntilIdle()
        assertEquals(testList, viewModel.state.value.trainers)
    }


}