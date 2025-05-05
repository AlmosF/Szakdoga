package com.example.szakdogafitness.Trainers

import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.data.trainees.ITraineesService
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
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
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


}