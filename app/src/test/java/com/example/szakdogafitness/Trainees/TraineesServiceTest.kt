package com.example.szakdogafitness.Trainees

import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.data.trainees.ITraineesService
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.trainees.AcceptRequestUseCase
import com.example.szakdogafitness.domain.usecases.trainees.AddEventToCalendarUseCase
import com.example.szakdogafitness.domain.usecases.trainees.AllTraineesUseCases
import com.example.szakdogafitness.domain.usecases.trainees.DeleteRequestUseCase
import com.example.szakdogafitness.domain.usecases.trainees.FetchRequestsUseCase
import com.example.szakdogafitness.domain.usecases.trainees.FetchUsersDailyDatasByIdUseCase
import com.example.szakdogafitness.domain.usecases.trainees.RemoveEventFromCalendarUseCase
import com.example.szakdogafitness.domain.usecases.trainees.RemoveTraineeUseCase
import com.example.szakdogafitness.feature.trainee.TraineesEvent
import com.example.szakdogafitness.feature.trainee.TraineesViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class TraineesServiceTest {

    private lateinit var viewModel: TraineesViewModel

    private val repository: ITraineesService = mockk()
    private val acceptRequests: AcceptRequestUseCase = mockk(relaxed = true)
    private val addEventToCalendar: AddEventToCalendarUseCase = mockk(relaxed = true)
    private val removeEventFromCalendar: RemoveEventFromCalendarUseCase = mockk(relaxed = true)
    private val deleteRequest: DeleteRequestUseCase = mockk(relaxed = true)
    private val removeTrainee: RemoveTraineeUseCase = mockk(relaxed = true)
    private val fetchRequests: FetchRequestsUseCase = mockk(relaxed = true)
    private val fetchUsersDailyDatasById: FetchUsersDailyDatasByIdUseCase = mockk(relaxed = true)

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setup(){

        Dispatchers.setMain(dispatcher)

        val allTraineesUseCases = AllTraineesUseCases(
            repository,
            acceptRequests,
            addEventToCalendar,
            removeEventFromCalendar,
            deleteRequest,
            removeTrainee,
            fetchRequests,
            fetchUsersDailyDatasById
        )
        viewModel = TraineesViewModel(
            allTraineesUseCases,
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun loadPendingRequestsAndAssertEqualTests() = runTest{
        val testList = listOf(
            Pair(Requests(traineeId = "1", status = "pending"), User(userId = "1")),
            Pair(Requests(traineeId = "2", status = "pending"), User(userId = "2")),
            Pair(Requests(traineeId = "3", status = "pending"), User(userId = "3")),
        )
        coEvery { fetchRequests.invoke("pending") } returns flowOf(testList)
        viewModel.onEvent(TraineesEvent.LoadPendingRequests)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(testList, viewModel.state.value.pendingRequests)
    }

    @Test
    fun loadAcceptedRequestsAndAssertEqualsTests() = runTest {
        val testList = listOf(
            Pair(Requests(traineeId = "1", status = "accepted"), User(userId = "1")),
            Pair(Requests(traineeId = "2", status = "accepted"), User(userId = "2")),
            Pair(Requests(traineeId = "3", status = "accepted"), User(userId = "3")),
        )
        coEvery { fetchRequests.invoke("accepted") } returns flowOf(testList)
        viewModel.onEvent(TraineesEvent.LoadAcceptedRequests)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(testList, viewModel.state.value.acceptedRequests)
    }

    @Test
    fun declinePendingRequestAndAssertEqualsTests() = runTest {
        val testList = listOf(
            Pair(Requests(traineeId = "1", status = "pending"), User(userId = "1")),
            Pair(Requests(traineeId = "2", status = "pending"), User(userId = "2")),
            Pair(Requests(traineeId = "3", status = "pending"), User(userId = "3")),
        )

        coEvery { deleteRequest.invoke(testList[0].first.traineeId) } returns Unit
        coEvery { fetchRequests.invoke("pending") } returns flowOf(testList)
        viewModel.onEvent(TraineesEvent.LoadPendingRequests)
        advanceUntilIdle()
        viewModel.onEvent(TraineesEvent.DeclineRequest(testList[0].first.traineeId))
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(testList, viewModel.state.value.pendingRequests)
    }
    @Test
    fun declineRequestShouldInvokeDeleteRequestUseCaseTest() = runTest {
        val traineeId = "testTraineeId"
        coEvery { deleteRequest.invoke(traineeId) } returns Unit

        viewModel.onEvent(TraineesEvent.DeclineRequest(traineeId))
        advanceUntilIdle()

        coVerify { deleteRequest.invoke(traineeId) }
    }

    @Test
    fun acceptRequestShouldInvokeAcceptRequestUseCaseTest() = runTest {
        val traineeId = "testTraineeId"
        coEvery { acceptRequests.invoke(traineeId) } returns Unit

        viewModel.onEvent(TraineesEvent.AcceptRequest(traineeId))
        advanceUntilIdle()

        coVerify { acceptRequests.invoke(traineeId) }
    }

    @Test
    fun selectTraineeShouldAssertEqualsTest() = runTest {
        val testTraineeId = "2"
        val testList = listOf(
            Pair(Requests(traineeId = "1", status = "pending"), User(userId = "1")),
            Pair(Requests(traineeId = "2", status = "pending"), User(userId = "2")),
            Pair(Requests(traineeId = "3", status = "pending"), User(userId = "3")),
        )
        coEvery { fetchRequests.invoke("pending") } returns flowOf(testList)
        viewModel.onEvent(TraineesEvent.LoadPendingRequests)
        advanceUntilIdle()
        viewModel.onEvent(TraineesEvent.SelectTrainee(testTraineeId))
        advanceUntilIdle()
        assertEquals(testTraineeId, viewModel.state.value.selectedTrainee?.first?.traineeId)
    }

    @Test
    fun fetchTraineeDailyData() = runTest {
        val testDailyData = listOf(
            DailyData(waterIntake = 1.1),
            DailyData(waterIntake = 2.2),
            DailyData(waterIntake = 3.3)
        )
        val testTraineeId = "1"
        coEvery { fetchUsersDailyDatasById.invoke(testTraineeId) } returns flowOf(testDailyData)

        viewModel.onEvent(TraineesEvent.FetchTraineesDailyData(testTraineeId))
        advanceUntilIdle()
        assertEquals(testDailyData, viewModel.state.value.traineesDailyDataList)
        //Just Update Now
        viewModel.onEvent(TraineesEvent.FetchTraineesDailyData(testTraineeId))
        advanceUntilIdle()
        assertEquals(testDailyData, viewModel.state.value.traineesDailyDataList)
    }

    @Test
    fun nextAndPreviousDailyDataTest() = runTest {
        val testDailyData = listOf(
            DailyData(waterIntake = 1.1),
            DailyData(waterIntake = 2.2),
            DailyData(waterIntake = 3.3)
        )
        val testTraineeId = "1"
        coEvery { fetchUsersDailyDatasById.invoke(testTraineeId) } returns flowOf(testDailyData)

        viewModel.onEvent(TraineesEvent.FetchTraineesDailyData(testTraineeId))
        advanceUntilIdle()
        assertEquals(testDailyData, viewModel.state.value.traineesDailyDataList)
        viewModel.onEvent(TraineesEvent.PreviousDailyDataDay)
        advanceUntilIdle()
        assertEquals(testDailyData[1], viewModel.state.value.selectedTraineesDailyData)
        viewModel.onEvent(TraineesEvent.NextDailyDataDay)
        advanceUntilIdle()
        assertEquals(testDailyData[2], viewModel.state.value.selectedTraineesDailyData)
    }

}