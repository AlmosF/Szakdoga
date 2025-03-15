package com.example.szakdogafitness.data.setup

import android.util.Log
import androidx.navigation.NavHostController
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.FirestoreTrainee
import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserEmailUseCase
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.example.szakdogafitness.presentation.navigation.setup.SetupScreenRoute
import com.example.szakdogafitness.viewmodel.SetupProfileState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SetupService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase,
    private val currentUserEmail: CurrentUserEmailUseCase
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

    /*override fun nextButtonEnabled(state: SetupProfileState): Boolean {
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
    }*/
    override fun nextButtonEnabled(state: SetupProfileState): Boolean {
        val currentScreen = state.currentScreen
        return if (currentScreen == 0){ // TODO: Date of Birth not checked
            state.user.firstName.isNotEmpty() && state.user.lastName.isNotEmpty()
                    && state.user.gender != 0 && state.user.phoneNumber.isNotEmpty()
                    && state.user.role != 0
        } else {
            when {
                currentScreen == 1 && state.user.role == 1 -> { // Trainer
                    state.user.trainer.certifications.isNotEmpty() && state.user.trainer.areasOfExperties.isNotEmpty()
                            &&state.user.trainer.trainingStyles.isNotEmpty() && state.user.trainer.experience != 0
                            && state.user.trainer.availability.isNotEmpty()
                }
                currentScreen == 1 && state.user.role == 2 -> { // Trainee TODO: Default values are buggy because of lists need to be -1
                    state.user.trainee.unit != 0 && state.user.trainee.height != 0 && state.user.trainee.weight != 0.0
                            && state.user.trainee.trainings != 0 && state.user.trainee.fitnessGoals.isNotEmpty()
                            && state.user.trainee.currentFitnessLevel != 0
                }
                currentScreen == 2 && state.user.role == 1 -> { // Trainer
                    state.user.trainer.pricing != 0 && state.user.bio.isNotEmpty()
                }
                currentScreen == 2 && state.user.role == 2 -> { // Trainee
                    state.user.trainee.healthInformation.isNotEmpty() && state.user.trainee.dietaryPreferences.isNotEmpty()
                            && state.user.bio.isNotEmpty()
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

    /*override suspend fun finishButtonClicked(state: SetupProfileState) {
        val userId = userIdUseCase()
        Log.d("FinishButtonClicked", "User id: $userId")
        Log.d("FinishButtonClicked", "state: $state")
        if (userId != null) {
            firestore.collection("users").add(
                FirestoreUser(
                    uid = userId,
                    firstName = state.user.firstName,
                    lastName = state.user.lastName,
                    dateOfBirth = state.user.dateOfBirth,
                    gender = state.user.gender,
                    phoneNumber = state.user.phoneNumber,
                    role = state.user.role,
                    bio = state.user.bio,
                    fcmToken = state.user.fcmToken,
                    profileImageUrl = state.user.profileImageUrl,
                    location = state.user.location,
                    email = currentUserEmail() ?: "",
                    setupDone = true
                )
            ).await()
            when(state.user.role) {
                1 -> {
                    firestore.collection("trainers").add(
                        FirestoreTrainer(
                            uid = userId,
                            certifications = state.user.trainer.certifications,
                            areasOfExperties = state.user.trainer.areasOfExperties,
                            trainingStyles = state.user.trainer.trainingStyles,
                            experience = state.user.trainer.experience,
                            availability = state.user.trainer.availability,
                            pricing = state.user.trainer.pricing,
                            rating = state.user.trainer.rating,
                            specializations = state.user.trainer.specializations,
                            socialMediaLinks = state.user.trainer.socialMediaLinks,
                            pendingRequests = state.user.trainer.pendingRequests,
                            acceptedRequests = state.user.trainer.acceptedRequests
                        )
                    ).await()
                }
                2 -> {
                    firestore.collection("trainees").add(
                        FirestoreTrainee(
                            uid = userId,
                            unit = state.user.trainee.unit,
                            height = state.user.trainee.height,
                            weight = state.user.trainee.weight,
                            trainings = state.user.trainee.trainings,
                            fitnessGoals = state.user.trainee.fitnessGoals,
                            currentFitnessLevel = state.user.trainee.currentFitnessLevel,
                            trainingPreference = state.user.trainee.trainingPreference,
                            healthInformation = state.user.trainee.healthInformation,
                            dietaryPreferences = state.user.trainee.dietaryPreferences,
                            availableEquipment = state.user.trainee.availableEquipment,
                            activeTrainers = state.user.trainee.activeTrainers
                        )
                    ).await()
                }
            }
            firestore.collection("dailyDatas").add(
                DailyDatas(
                    uid = userId,
                    dailyData = listOf(
                        DailyData(
                            date = Timestamp.now()
                        )
                    )
                )
            )
        } else {
            Log.d("SetupService", "User id is null")
        }
    }*/
    override suspend fun finishButtonClicked(state: SetupProfileState) {
        val userId = userIdUseCase()
        Log.d("FinishButtonClicked", "User id: $userId")
        Log.d("FinishButtonClicked", "state: $state")
        if (userId != null) {
            firestore.collection("users").document(userId).set(
                FirestoreUser(
                    uid = userId,
                    firstName = state.user.firstName,
                    lastName = state.user.lastName,
                    dateOfBirth = state.user.dateOfBirth,
                    gender = state.user.gender,
                    phoneNumber = state.user.phoneNumber,
                    role = state.user.role,
                    bio = state.user.bio,
                    fcmToken = state.user.fcmToken,
                    profileImageUrl = state.user.profileImageUrl,
                    location = state.user.location,
                    email = currentUserEmail() ?: "",
                    setupDone = true
                )
            ).await()
            when(state.user.role) {
                1 -> {
                    firestore.collection("trainers").document(userId).set(
                        FirestoreTrainer(
                            uid = userId,
                            certifications = state.user.trainer.certifications,
                            areasOfExperties = state.user.trainer.areasOfExperties,
                            trainingStyles = state.user.trainer.trainingStyles,
                            experience = state.user.trainer.experience,
                            availability = state.user.trainer.availability,
                            pricing = state.user.trainer.pricing,
                            rating = state.user.trainer.rating,
                            specializations = state.user.trainer.specializations,
                            socialMediaLinks = state.user.trainer.socialMediaLinks,
                            pendingRequests = state.user.trainer.pendingRequests,
                            acceptedRequests = state.user.trainer.acceptedRequests
                        )
                    ).await()
                }
                2 -> {
                    firestore.collection("trainees").document(userId).set(
                        FirestoreTrainee(
                            uid = userId,
                            unit = state.user.trainee.unit,
                            height = state.user.trainee.height,
                            weight = state.user.trainee.weight,
                            trainings = state.user.trainee.trainings,
                            fitnessGoals = state.user.trainee.fitnessGoals,
                            currentFitnessLevel = state.user.trainee.currentFitnessLevel,
                            trainingPreference = state.user.trainee.trainingPreference,
                            healthInformation = state.user.trainee.healthInformation,
                            dietaryPreferences = state.user.trainee.dietaryPreferences,
                            availableEquipment = state.user.trainee.availableEquipment,
                            activeTrainers = state.user.trainee.activeTrainers
                        )
                    ).await()
                }
            }
            firestore.collection("dailyDatas").document(userId).set(
                DailyDatas(
                    uid = userId,
                    dailyData = listOf(
                        DailyData(
                            date = Timestamp.now()
                        )
                    )
                )
            )
        } else {
            Log.d("SetupService", "User id is null")
        }
    }


}