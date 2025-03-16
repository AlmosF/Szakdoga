package com.example.szakdogafitness.viewmodel

import android.R.attr.height
import android.util.Log
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.setup.AllSetupUseCases
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import com.example.szakdogafitness.presentation.navigation.setup.SetupScreenRoute
import com.example.szakdogafitness.util.UiEvent
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val setupUseCase: AllSetupUseCases
) : ViewModel(){

    private lateinit var navHostController: NavHostController

    fun setNavController(navHostController: NavHostController){
        this.navHostController = navHostController
    }

    private val _state = MutableStateFlow(SetupProfileState())
    val state = _state.asStateFlow()

    private val _isNextButtonEnabled = MutableStateFlow(false)
    val isNextButtonEnabled = _isNextButtonEnabled.asStateFlow()

    private val _isPreviousButtonVisible = MutableStateFlow(false)
    val isPreviousButtonVisible = _isPreviousButtonVisible.asStateFlow()

    private val _nextButtonLabel = MutableStateFlow("Next")
    val nextButtonLabel = _nextButtonLabel.asStateFlow()


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun incrementBarStatus() {
        /*val doneFieldsTrainer = listOf( // 13 for trainer, 15 for trainee
            _state.value.firstName,
            _state.value.lastName,
            _state.value.dateOfBirth,
            _state.value.gender,
            _state.value.phoneNumber,
            _state.value.role,
            _state.value.certifications,
            _state.value.areasOfExperties,
            _state.value.trainingStyles,
            _state.value.availability,
            _state.value.experience,
            _state.value.pricing,
            _state.value.bio,
        ).count { it != null && it != "" && it != emptyList<Any>() && it != emptyMap<Any, Any>() && it != 0 }*/

        val doneFieldsTrainer = listOf( // 13 for trainer, 15 for trainee
            _state.value.user.firstName,
            _state.value.user.lastName,
            _state.value.user.dateOfBirth,
            _state.value.user.gender,
            _state.value.user.phoneNumber,
            _state.value.user.role,
            _state.value.user.trainer.certifications,
            _state.value.user.trainer.areasOfExperties,
            _state.value.user.trainer.trainingStyles,
            _state.value.user.trainer.availability,
            _state.value.user.trainer.experience,
            _state.value.user.trainer.pricing,
            _state.value.user.bio,
        ).count { it != null && it != "" && it != emptyList<Any>() && it != emptyMap<Any, Any>() && it != 0 }

        /*val doneFieldsTrainee = listOf( // 13 for trainer, 15 for trainee
            _state.value.firstName,
            _state.value.lastName,
            _state.value.dateOfBirth,
            _state.value.gender,
            _state.value.phoneNumber,
            _state.value.role,
            _state.value.unit,
            _state.value.height,
            _state.value.weight,
            _state.value.trainings,
            _state.value.fitnessGoals,
            _state.value.currentFitnessLevel,
            _state.value.healthInformation,
            _state.value.dietaryPreferences,
            _state.value.bio,
        ).count { it != null && it != "" && it != emptyList<Any>() && it != emptyMap<Any, Any>() && it != 0 }*/
        val doneFieldsTrainee = listOf( // 13 for trainer, 15 for trainee
            _state.value.user.firstName,
            _state.value.user.lastName,
            _state.value.user.dateOfBirth,
            _state.value.user.gender,
            _state.value.user.phoneNumber,
            _state.value.user.role,
            _state.value.user.trainee.unit,
            _state.value.user.trainee.height,
            _state.value.user.trainee.weight,
            _state.value.user.trainee.trainings,
            _state.value.user.trainee.fitnessGoals,
            _state.value.user.trainee.currentFitnessLevel,
            _state.value.user.trainee.healthInformation,
            _state.value.user.trainee.dietaryPreferences,
            _state.value.user.bio,
        ).count { it != null && it != "" && it != emptyList<Any>() && it != emptyMap<Any, Any>() && it != 0 }

        //if (_state.value.role == 1) _state.value = _state.value.copy(barStatus = doneFieldsTrainer / 13.0)
        if (_state.value.user.role == 1) _state.value = _state.value.copy(barStatus = doneFieldsTrainer / 13.0)
        else _state.value = _state.value.copy(barStatus = doneFieldsTrainee / 15.0)

        //_state.value = _state.value.copy(barStatus = doneFieldsTrainer / 12.0)
    }

    fun onEvent(event: SetupEvent/*, navHostController: NavHostController*/) {
        when(event){
            is SetupEvent.AreasOfExpertiesChanged ->{
                //_state.value = _state.value.copy(areasOfExperties = event.areasOfExperties)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            areasOfExperties = event.areasOfExperties
                        )
                    )
                )
            }
            is SetupEvent.AvailabilityChanged -> {
                //_state.value = _state.value.copy(availability = event.availability)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            availability = event.availability
                        )
                    )
                )
            }
            is SetupEvent.AvailableEquipmentChanged -> {
                //_state.value = _state.value.copy(availableEquipment = event.availableEquipment)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            availableEquipment = event.availableEquipment
                        )
                    )
                )
            }
            is SetupEvent.BioChanged -> {
                //_state.value = _state.value.copy(bio = event.bio)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        bio = event.bio
                    )
                )
            }
            is SetupEvent.CertificationsChanged -> {
                //_state.value = _state.value.copy(certifications = event.certifications)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            certifications = event.certifications
                        )
                    )
                )
            }
            is SetupEvent.CurrentFitnessLevelChanged -> {
                //_state.value = _state.value.copy(currentFitnessLevel = event.currentFitnessLevel)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            currentFitnessLevel = event.currentFitnessLevel
                        )
                    )
                )
            }
            is SetupEvent.CurrentScreenChanged -> {
                //_state.value = _state.value.copy(currentScreen = event.currentScreen)
                _state.value = _state.value.copy(
                    currentScreen = event.currentScreen
                )
            }
            is SetupEvent.DateOfBirthChanged -> {
                //_state.value = _state.value.copy(dateOfBirth = event.dateOfBirth)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        dateOfBirth = event.dateOfBirth
                    )
                )
            }
            is SetupEvent.DietaryPreferencesChanged -> {
                //_state.value = _state.value.copy(dietaryPreferences = event.dietaryPreferences)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            dietaryPreferences = event.dietaryPreferences
                        )
                    )
                )
            }
            is SetupEvent.ExperienceChanged -> {
                //_state.value = _state.value.copy(experience = event.experience)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            experience = event.experience
                        )
                    )
                )
            }
            is SetupEvent.FcmTokenChanged -> {
                //_state.value = _state.value.copy(fcmToken = event.fcmToken)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        fcmToken = event.fcmToken
                    )
                )
            }
            is SetupEvent.FirstNameChanged -> {
                //_state.value = _state.value.copy(firstName = event.firstName)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        firstName = event.firstName
                    )
                )
            }
            is SetupEvent.FitnessGoalsChanged -> {
                //_state.value = _state.value.copy(fitnessGoals = event.fitnessGoals)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            fitnessGoals = event.fitnessGoals
                        )
                    )
                )
            }
            is SetupEvent.GenderChanged -> {
                //_state.value = _state.value.copy(gender = event.gender)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        gender = event.gender
                    )
                )
            }
            is SetupEvent.HealthInformationChanged -> {
                //_state.value = _state.value.copy(healthInformation = event.healthInformation)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            healthInformation = event.healthInformation
                        )
                    )
                )
            }
            is SetupEvent.HeightChanged -> {
                //_state.value = _state.value.copy(height = event.height)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            height = event.height
                        )
                    )
                )
            }
            is SetupEvent.LastNameChanged -> {
                //_state.value = _state.value.copy(lastName = event.lastName)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        lastName = event.lastName
                    )
                )
            }
            is SetupEvent.LocationChanged -> {
                //_state.value = _state.value.copy(location = event.location)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        location = event.location
                    )
                )
            }
            is SetupEvent.PhoneNumberChanged -> {
                //_state.value = _state.value.copy(phoneNumber = event.phoneNumber)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        phoneNumber = event.phoneNumber
                    )
                )
            }
            is SetupEvent.PricingChanged -> {
                //_state.value = _state.value.copy(pricing = event.pricing)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            pricing = event.pricing
                        )
                    )
                )
            }
            is SetupEvent.ProfileImageUrlChanged -> {
                //_state.value = _state.value.copy(profileImageUrl = event.profileImageUrl)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        profileImageUrl = event.profileImageUrl
                    )
                )
            }
            is SetupEvent.RatingChanged -> {
                //_state.value = _state.value.copy(rating = event.rating)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            rating = event.rating
                        )
                    )
                )
            }
            is SetupEvent.RoleChanged -> {
                //_state.value = _state.value.copy(role = event.role)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        role = event.role
                    )
                )
            }
            is SetupEvent.SocialMediaLinksChanged -> {
                //_state.value = _state.value.copy(socialMediaLinks = event.socialMediaLinks)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            socialMediaLinks = event.socialMediaLinks
                        )
                    )
                )
            }
            is SetupEvent.SpecializationsChanged -> {
                //_state.value = _state.value.copy(specializations = event.specializations)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            specializations = event.specializations
                        )
                    )
                )
            }
            is SetupEvent.TrainingPreferenceChanged -> {
                //_state.value = _state.value.copy(trainingPreference = event.trainingPreference)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            trainingPreference = event.trainingPreference
                        )
                    )
                )
            }
            is SetupEvent.TrainingStylesChanged -> {
                //_state.value = _state.value.copy(trainingStyles = event.trainingStyles)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainer = _state.value.user.trainer.copy(
                            trainingStyles = event.trainingStyles
                        )
                    )
                )
            }
            is SetupEvent.TrainingsChanged -> {
                //_state.value = _state.value.copy(trainings = event.trainings)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            trainings = event.trainings
                        )
                    )
                )
            }
            is SetupEvent.UnitChanged -> {
                //_state.value = _state.value.copy(unit = event.unit)
                val newWeight = when(event.unit){
                    1 -> _state.value.user.trainee.weight / 2.20462
                    2 -> _state.value.user.trainee.weight * 2.20462
                    else -> 0.0
                }.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            unit = event.unit,
                            weight = newWeight
                        )
                    )
                )
            }
            is SetupEvent.WeightChanged -> {
                //_state.value = _state.value.copy(weight = event.weight)
                _state.value = _state.value.copy(
                    user = _state.value.user.copy(
                        trainee = _state.value.user.trainee.copy(
                            weight = event.weight
                        )
                    )
                )
            }
            is SetupEvent.CurrentBarStatusChanged -> {
                _state.value = _state.value.copy(barStatus = event.barStatus)
            }
            SetupEvent.BackButtonClicked -> {
                /*navigationUseCase.navigateBack(
                    navHostController = navHostController,
                    currentScreen = _state.value.currentScreen
                )*/
                setupUseCase.previousScreen(
                    currentScreen = _state.value.currentScreen,
                    //role = _state.value.role,
                    role = _state.value.user.role,
                    navHostController = navHostController
                )
                _state.value = _state.value.copy(currentScreen = _state.value.currentScreen - 1)
                /*navigateBack(
                    navHostController = navHostController
                )*/
            }

            SetupEvent.NextButtonClicked -> {
                Log.d("SetupViewModel", "NextButtonClicked")
                /*navigationUseCase.navigateNext(
                    navHostController = navHostController,
                    currentScreen = _state.value.currentScreen,
                    role = _state.value.role
                )*/
                setupUseCase.nextScreen(
                    currentScreen = _state.value.currentScreen,
                    //role = _state.value.role,
                    role = _state.value.user.role,
                    navHostController = navHostController
                )
                _state.value = _state.value.copy(currentScreen = _state.value.currentScreen + 1)
                /*navigateNext(
                    navHostController = navHostController
                )*/
            }
            SetupEvent.FinishButtonClicked -> {
                Log.d("SetupViewModel", "FinishButtonClicked")
                onFinishButton(_state.value)
            }
            SetupEvent.SaveButtonClicked -> TODO()
        }
        incrementBarStatus()
        updateButtonsState()
        //Log.d("SetupViewModel", "State: ${_state.value.availability}")
        Log.d("SetupViewModel", "State: ${_state.value.user.trainer.availability}")
    }

    private fun onFinishButton(state: SetupProfileState) {
        viewModelScope.launch(Dispatchers.IO){
            setupUseCase.finishButton(state)
        }
    }

    private fun updateButtonsState() {
        _isPreviousButtonVisible.value = setupUseCase.previousButtonVisible(
            currentScreen = _state.value.currentScreen
        )
        _isNextButtonEnabled.value = setupUseCase.nextButtonEnabled(
            state = _state.value
        )
        _nextButtonLabel.value = setupUseCase.nextButtonLabel(
            currentScreen = _state.value.currentScreen
        )
    }

    fun navigateNext(navHostController: NavHostController) {
        val currentScreen = _state.value.currentScreen
        //val role = _state.value.role
        val role = _state.value.user.role
        val nextScreen = when (currentScreen) {
            0 -> if (role == 1) SetupScreenRoute.TrainerOne.route else SetupScreenRoute.TraineeOne.route
            1 -> if (role == 1) SetupScreenRoute.TrainerTwo.route else SetupScreenRoute.TraineeTwo.route
            else -> null
        }
        nextScreen?.let {
            _state.value = _state.value.copy(currentScreen = currentScreen + 1)
            navHostController.navigate(it)
        }
    }

    fun navigateBack(navHostController: NavHostController) {
        val currentScreen = _state.value.currentScreen
        if (currentScreen > 0) {
            _state.value = _state.value.copy(currentScreen = currentScreen - 1)
            navHostController.popBackStack()
        }
    }

}


sealed class SetupEvent{
    data class FirstNameChanged(val firstName: String) : SetupEvent()
    data class LastNameChanged(val lastName: String) : SetupEvent()
    data class DateOfBirthChanged(val dateOfBirth: Timestamp) : SetupEvent()
    data class GenderChanged(val gender: Int) : SetupEvent()
    data class PhoneNumberChanged(val phoneNumber: String) : SetupEvent()
    data class RoleChanged(val role: Int) : SetupEvent()
    data class BioChanged(val bio: String) : SetupEvent()
    data class FcmTokenChanged(val fcmToken: String) : SetupEvent()
    data class ProfileImageUrlChanged(val profileImageUrl: String) : SetupEvent()
    data class LocationChanged(val location: GeoPoint) : SetupEvent()
    //trainer
    data class CertificationsChanged(val certifications: List<Int>) : SetupEvent()
    data class AreasOfExpertiesChanged(val areasOfExperties: List<Int>) : SetupEvent()
    data class TrainingStylesChanged(val trainingStyles: List<Int>) : SetupEvent()
    data class ExperienceChanged(val experience: Int) : SetupEvent()
    data class AvailabilityChanged(val availability: Map<String, List<Int>>) : SetupEvent()
    data class PricingChanged(val pricing: Int) : SetupEvent()
    data class RatingChanged(val rating: Int) : SetupEvent()
    data class SpecializationsChanged(val specializations: List<Int>) : SetupEvent()
    data class SocialMediaLinksChanged(val socialMediaLinks: Map<String, String>) : SetupEvent()
    //trainee
    data class UnitChanged(val unit: Int) : SetupEvent()
    data class HeightChanged(val height: Int) : SetupEvent()
    data class WeightChanged(val weight: Double) : SetupEvent()
    data class TrainingsChanged(val trainings: Int) : SetupEvent()
    data class FitnessGoalsChanged(val fitnessGoals: List<Int>) : SetupEvent()
    data class CurrentFitnessLevelChanged(val currentFitnessLevel: Int) : SetupEvent()
    data class TrainingPreferenceChanged(val trainingPreference: Int) : SetupEvent()
    data class HealthInformationChanged(val healthInformation: List<Int>) : SetupEvent()
    data class DietaryPreferencesChanged(val dietaryPreferences: List<Int>) : SetupEvent()
    data class AvailableEquipmentChanged(val availableEquipment: List<Int>) : SetupEvent()
    //screen
    data class CurrentScreenChanged(val currentScreen: Int) : SetupEvent()
    data class CurrentBarStatusChanged(val barStatus: Double) : SetupEvent()
    //buttons
    object BackButtonClicked : SetupEvent()
    object NextButtonClicked : SetupEvent()
    object FinishButtonClicked : SetupEvent()
    object SaveButtonClicked : SetupEvent()
}


//based on User.kt
data class SetupProfileState(
    /*var userId: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var dateOfBirth: Timestamp? = null,
    var gender: Int = 0,
    var phoneNumber: String = "",
    var role: Int = 0,
    var email: String = "",
    var bio: String = "",
    var fcmToken: String = "",
    var profileImageUrl: String = "",
    var location: GeoPoint? = null,
    //trainer
    var certifications: List<Int> = emptyList(),
    var areasOfExperties: List<Int> = emptyList(),
    var trainingStyles: List<Int> = emptyList(),
    var experience: Int = 0,
    var availability: Map<String, List<Int>> = emptyMap(),
    var pricing: Int = 0,
    var rating: Float = 0f,
    var specializations: List<Int> = emptyList(),
    var socialMediaLinks: Map<String, String> = emptyMap(),
    //trainee
    var unit: Int = 0,
    var height: Int = 0,
    var weight: Double = 0.0,
    var trainings: Int = 0,
    var fitnessGoals: List<Int> = emptyList(),
    var currentFitnessLevel: Int = 0, //Vagy int?
    var trainingPreference: Int = 0,
    var healthInformation: List<String> = emptyList(),
    var dietaryPreferences: List<String> = emptyList(),
    var availableEquipment: List<String> = emptyList(),*/
    var user: User = User(),
    //screen
    var currentScreen: Int = 0, //0: RoleSelection, 1: TrainerOne, 2: TrainerTwo, 3: TraineeOne, 4: TraineeTwo
    //bar
    var barStatus: Double = 0.0
)