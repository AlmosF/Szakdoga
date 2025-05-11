package com.example.szakdogafitness.Setup

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.szakdogafitness.data.setup.ISetupService
import com.example.szakdogafitness.domain.usecases.setup.AllSetupUseCases
import com.example.szakdogafitness.domain.usecases.setup.FinishButtonUseCase
import com.example.szakdogafitness.domain.usecases.setup.NextButtonEnabledUseCase
import com.example.szakdogafitness.domain.usecases.setup.NextButtonLabelUseCase
import com.example.szakdogafitness.domain.usecases.setup.NextScreenUseCase
import com.example.szakdogafitness.domain.usecases.setup.PreviousButtonVisibleUseCase
import com.example.szakdogafitness.domain.usecases.setup.PreviousScreenUseCase
import com.example.szakdogafitness.feature.setup.SetupEvent
import com.example.szakdogafitness.feature.setup.SetupViewModel
import com.google.firebase.Timestamp
import io.mockk.MockKMatcherScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.math.RoundingMode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

//@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SetupStatesTest {

    private lateinit var viewModel: SetupViewModel

    private val repository: ISetupService = mockk()
    private val nextScreen = mockk<NextScreenUseCase>()
    private val previousScreen = mockk<PreviousScreenUseCase>()
    private val nextButtonEnabled = mockk<NextButtonEnabledUseCase>()
    private val previousButtonVisible = mockk<PreviousButtonVisibleUseCase>()
    private val nextButtonLabel = mockk<NextButtonLabelUseCase>()
    private val finishButton = mockk<FinishButtonUseCase>(relaxed = true)


    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup(){
        Dispatchers.setMain(dispatcher)

        val setupUseCases = AllSetupUseCases(
            repository,
            nextScreen,
            previousScreen,
            nextButtonEnabled,
            previousButtonVisible,
            nextButtonLabel,
            finishButton
        )

        viewModel = SetupViewModel(
            setupUseCases
        )
    }

    @AfterTest
    fun tearDown(){
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun onFirstSetupScreenFieldsChangedAssertEqualsWithStatusBarAndButtonStatuses() {
        val firstName = "Test"
        val lastName = "Tester"
        //val dateOfBirth = Timestamp.now()
        val dateOfBirth = mockk<Timestamp>(relaxed = true)
        val gender = 1
        val phoneNumber = "123456789"
        val role = 1
        every { nextButtonEnabled.invoke(any()) } returns true
        every { previousButtonVisible.invoke(any()) } returns false
        every { nextButtonLabel.invoke(any()) } returns "Next"

        viewModel.onEvent(SetupEvent.FirstNameChanged(firstName))
        viewModel.onEvent(SetupEvent.LastNameChanged(lastName))
        viewModel.onEvent(SetupEvent.DateOfBirthChanged(dateOfBirth))
        viewModel.onEvent(SetupEvent.GenderChanged(gender))
        viewModel.onEvent(SetupEvent.PhoneNumberChanged(phoneNumber))
        viewModel.onEvent(SetupEvent.RoleChanged(role))
        val barStatus = viewModel.state.value.barStatus
        val expectedBarStatus = 6/13.toDouble()
        assertEquals(expectedBarStatus, barStatus)
        assertEquals(true, viewModel.isNextButtonEnabled.value)
        assertEquals(false, viewModel.isPreviousButtonVisible.value)
        assertEquals("Next", viewModel.nextButtonLabel.value)
    }

    @Test
    fun onLastSetupScreenFieldsChangedAssertEqualsWithStatusBarAndButtonStates() = runTest{
        val firstName = "Test"
        val lastName = "Tester"
        val dateOfBirth = Timestamp.now()
        val gender = 1
        val phoneNumber = "123456789"
        val role = 1
        val certifications = listOf(1,2,3)
        val areasOfE = listOf(1,2)
        val trainingS = listOf(1,2)
        val avaliability = mapOf<String, List<Int>>("Monday" to listOf(1,2))
        val availableEquipment = listOf(1,2)
        val trainingExperience = 2000
        val pricing = 1000
        val bio = "lorem ipsum"

        every { nextButtonEnabled.invoke(any()) } returns true
        every { previousButtonVisible.invoke(any()) } returns true
        every { nextButtonLabel.invoke(any()) } returns "Finish"

        viewModel.onEvent(SetupEvent.FirstNameChanged(firstName))
        viewModel.onEvent(SetupEvent.LastNameChanged(lastName))
        viewModel.onEvent(SetupEvent.DateOfBirthChanged(dateOfBirth))
        viewModel.onEvent(SetupEvent.GenderChanged(gender))
        viewModel.onEvent(SetupEvent.PhoneNumberChanged(phoneNumber))
        viewModel.onEvent(SetupEvent.RoleChanged(role))
        viewModel.onEvent(SetupEvent.CertificationsChanged(certifications))
        viewModel.onEvent(SetupEvent.AreasOfExpertiesChanged(areasOfE))
        viewModel.onEvent(SetupEvent.TrainingStylesChanged(trainingS))
        viewModel.onEvent(SetupEvent.AvailabilityChanged(avaliability))
        viewModel.onEvent(SetupEvent.AvailableEquipmentChanged(availableEquipment))
        viewModel.onEvent(SetupEvent.ExperienceChanged(trainingExperience))
        viewModel.onEvent(SetupEvent.PricingChanged(pricing))
        viewModel.onEvent(SetupEvent.BioChanged(bio))

        val barStatus = viewModel.state.value.barStatus
        assertEquals(1.0, barStatus)
        assertEquals(true, viewModel.isNextButtonEnabled.value)
        assertEquals(true, viewModel.isPreviousButtonVisible.value)
        assertEquals("Finish", viewModel.nextButtonLabel.value)

    }

    @Test
    fun onRestFieldChangedAssertingEqualsBetweenStates() = runTest {

        val height = 160
        val weight = 60.0
        val socialMediaLinks = mapOf<String, String>("Instagram" to "test")
        val specializations = listOf(1,2,3)
        val trainingGoals = listOf(1,2)
        val trainingPreferences = 1
        val trainings = 1
        val healthInf = listOf(1)
        val currentFLevel = 1
        val dietaryPref = listOf(1)

        every { nextButtonEnabled.invoke(any()) } returns true
        every { previousButtonVisible.invoke(any()) } returns true
        every { nextButtonLabel.invoke(any()) } returns "Finish"

        viewModel.onEvent(SetupEvent.HeightChanged(height))
        viewModel.onEvent(SetupEvent.WeightChanged(weight))
        viewModel.onEvent(SetupEvent.SocialMediaLinksChanged(socialMediaLinks))
        viewModel.onEvent(SetupEvent.SpecializationsChanged(specializations))
        viewModel.onEvent(SetupEvent.FitnessGoalsChanged(trainingGoals))
        viewModel.onEvent(SetupEvent.TrainingPreferenceChanged(trainingPreferences))
        viewModel.onEvent(SetupEvent.TrainingsChanged(trainings))
        viewModel.onEvent(SetupEvent.HealthInformationChanged(healthInf))
        viewModel.onEvent(SetupEvent.CurrentFitnessLevelChanged(currentFLevel))
        viewModel.onEvent(SetupEvent.DietaryPreferencesChanged(dietaryPref))
        assertEquals(
            "test",
            viewModel.state.value.user.trainer.socialMediaLinks["Instagram"]
        )
        assertEquals(160, viewModel.state.value.user.trainee.height)
        viewModel.onEvent(SetupEvent.UnitChanged(2))
        assertEquals(
            (weight * 2.2046).toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble(),
            viewModel.state.value.user.trainee.weight
        )
    }

    @Test
    fun nextButtonClickedAndCallsNextScreen() = runTest {
        every { nextScreen.invoke(any(),any(),any()) } returns Unit
        every { previousScreen.invoke(any(), any(), any()) } returns Unit
        every { previousButtonVisible.invoke(any()) } returns true
        every { nextButtonEnabled.invoke(any()) } returns true
        every { nextButtonLabel.invoke(any()) } returns "Finish"

        viewModel.onEvent(SetupEvent.RoleChanged(1))
        val initialScreen = 0
        val navHostController = mockk<NavHostController>(relaxed = true)
        viewModel.setNavController(navHostController)
        viewModel.onEvent(SetupEvent.NextButtonClicked)


        verify { nextScreen(initialScreen, 1, navHostController) }
        assertEquals(1, viewModel.state.value.currentScreen)

    }
}