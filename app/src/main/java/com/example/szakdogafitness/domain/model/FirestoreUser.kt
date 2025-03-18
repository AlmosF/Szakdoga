package com.example.szakdogafitness.domain.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint


data class FirestoreUser(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: Timestamp? = null,
    val gender: Int = 0,
    val phoneNumber: String = "",
    val role: Int = 0,
    val email: String = "",
    val bio: String = "",
    val fcmToken: String = "",
    val profileImageUrl: String = "",
    val location: GeoPoint? = null,
    val setupDone: Boolean = false
)

data class FirestoreTrainer(
    val uid: String = "",
    val certifications: List<Int> = emptyList(),
    val areasOfExperties: List<Int> = emptyList(),
    val trainingStyles: List<Int> = emptyList(),
    val experience: Int = 0,
    val availability: Map<String, List<Int>> = emptyMap(),
    val pricing: Int = 0,
    val rating: Int = 0,
    val specializations: List<Int> = emptyList(),
    val socialMediaLinks: Map<String, String> = emptyMap(),
    val pendingRequests: List<String> = emptyList(),
    val acceptedRequests: List<String> = emptyList(),
)

data class FirestoreTrainee(
    val uid: String = "",
    val unit: Int = 0,
    val height: Int = 0,
    val weight: Double = 0.0,
    val trainings: Int = 0,
    val fitnessGoals: List<Int> = emptyList(),
    val currentFitnessLevel: Int = 0,
    val trainingPreference: Int = 0,
    val healthInformation: List<Int> = emptyList(),
    val dietaryPreferences: List<Int> = emptyList(),
    val availableEquipment: List<Int> = emptyList(),
    val activeTrainers: List<String> = emptyList(),
)

fun FirestoreUser.toUser(trainer: FirestoreTrainer): User {
    Log.d("FirestoreUser", "ToUserCalled")
    return User(
        userId = uid,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        gender = gender,
        phoneNumber = phoneNumber,
        role = role,
        email = email,
        bio = bio,
        fcmToken = fcmToken,
        profileImageUrl = profileImageUrl,
        location = location,
        trainer = Trainer(
            certifications = trainer.certifications,
            areasOfExperties = trainer.areasOfExperties,
            trainingStyles = trainer.trainingStyles,
            experience = trainer.experience,
            availability = trainer.availability,
            pricing = trainer.pricing,
            rating = trainer.rating,
            specializations = trainer.specializations,
            socialMediaLinks = trainer.socialMediaLinks,
            pendingRequests = trainer.pendingRequests,
            acceptedRequests = trainer.acceptedRequests
        ),
        isSetupDone = setupDone,
        isAdmin = email == "feketealmos0@gmail.com"
    )
}

fun FirestoreUser.toUser(trainee: FirestoreTrainee): User {
    Log.d("FirestoreUser", "ToUserCalledd")
    return User(
        userId = uid,
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = dateOfBirth,
        gender = gender,
        phoneNumber = phoneNumber,
        role = role,
        email = email,
        bio = bio,
        fcmToken = fcmToken,
        profileImageUrl = profileImageUrl,
        location = location,
        trainee = Trainee(
            unit = trainee.unit,
            height = trainee.height,
            weight = trainee.weight,
            trainings = trainee.trainings,
            fitnessGoals = trainee.fitnessGoals,
            currentFitnessLevel = trainee.currentFitnessLevel,
            trainingPreference = trainee.trainingPreference,
            healthInformation = trainee.healthInformation,
            dietaryPreferences = trainee.dietaryPreferences,
            availableEquipment = trainee.availableEquipment,
            activeTrainers = trainee.activeTrainers
        ),
        isSetupDone = setupDone,
        isAdmin = email == "feketealmos0@gmail.com"
    )
}
