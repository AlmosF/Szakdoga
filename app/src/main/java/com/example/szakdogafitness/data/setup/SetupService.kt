package com.example.szakdogafitness.data.setup

import android.util.Log
import androidx.navigation.NavHostController
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.example.szakdogafitness.presentation.navigation.setup.SetupScreenRoute
import com.example.szakdogafitness.viewmodel.SetupProfileState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SetupService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase
) : ISetupService {

    override fun nextScreen(currentScreen: Int, role: Int, navHostController: NavHostController) {
        val nextScreen = when (currentScreen) {
            0 -> if (role == 1) SetupScreenRoute.TrainerOne.route else SetupScreenRoute.TraineeOne.route
            1 -> if (role == 1) SetupScreenRoute.TrainerTwo.route else SetupScreenRoute.TraineeTwo.route
            else -> {
                null
            }
        }

        nextScreen?.let {
            navHostController.navigate(it)
        }
    }

    override fun previousScreen(currentScreen: Int, role: Int, navHostController: NavHostController) {
        if (currentScreen > 0) {
            navHostController.popBackStack()
        }
    }

    override fun nextButtonEnabled(state: SetupProfileState): Boolean {
        val currentScreen = state.currentScreen
        return if (currentScreen == 0){ // TODO: Date of Birth not checked
            state.firstName.isNotEmpty() && state.lastName.isNotEmpty()
                    && state.gender != 0 && state.phoneNumber.isNotEmpty()
                    && state.role != 0
        } else {
            when {
                currentScreen == 1 && state.role == 1 -> { // Trainer
                    state.certifications.isNotEmpty() && state.areasOfExperties.isNotEmpty()
                            &&state.trainingStyles.isNotEmpty() && state.experience != 0
                            && state.availability.isNotEmpty()
                }
                currentScreen == 1 && state.role == 2 -> { // Trainee TODO: Default values are buggy because of lists need to be -1
                    state.unit != 0 && state.height != 0 && state.weight != 0.0
                            && state.trainings != 0 && state.fitnessGoals.isNotEmpty()
                            && state.currentFitnessLevel != 0
                }
                currentScreen == 2 && state.role == 1 -> { // Trainer
                    state.pricing != 0 && state.bio.isNotEmpty()
                }
                currentScreen == 2 && state.role == 2 -> { // Trainee
                    state.healthInformation.isNotEmpty() && state.dietaryPreferences.isNotEmpty()
                            && state.bio.isNotEmpty()
                }
                else -> {
                    return false
                }
            }
        }
    }

    override fun previousButtonVisible(currentScreen: Int): Boolean {
        return currentScreen > 0
    }

    override fun nextButtonLabel(currentScreen: Int): String {
        return if (currentScreen == 2) "Finish" else "Next"
    }

    override suspend fun finishButtonClicked(state: SetupProfileState) {
        val userId = userIdUseCase()
        if (userId != null) {
            firestore.collection("users").add(
                User(
                    userId = userId,
                    firstName = state.firstName,
                    lastName = state.lastName,
                    dateOfBirth = state.dateOfBirth,
                    gender = state.gender,
                    phoneNumber = state.phoneNumber,
                    role = state.role,
                    bio = state.bio,
                    certifications = state.certifications,
                    areasOfExperties = state.areasOfExperties,
                    trainingStyles = state.trainingStyles,
                    experience = state.experience,
                    availability = state.availability,
                    pricing = state.pricing,
                    specializations = state.specializations,
                    socialMediaLinks = state.socialMediaLinks,
                    unit = state.unit,
                    height = state.height,
                    weight = state.weight,
                    trainings = state.trainings,
                    fitnessGoals = state.fitnessGoals,
                    currentFitnessLevel = state.currentFitnessLevel,
                    trainingPreference = state.trainingPreference,
                    healthInformation = state.healthInformation,
                    dietaryPreferences = state.dietaryPreferences,
                    availableEquipment = state.availableEquipment,
                    isSetupDone = true
                    )
            ).await()
        } else {
            Log.d("SetupService", "User id is null")
        }
    }


}