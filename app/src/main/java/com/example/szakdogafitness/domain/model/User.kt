package com.example.szakdogafitness.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint


/**
 * User data class
 * @param userId: String (Firebase Auth UID)
 * @param firstName: String (First name of the user)
 * @param lastName: String (Last name of the user)
 * @param dateOfBirth: Timestamp? (Date of birth of the user
 * @param gender: Int (Gender of the user - 0: Not selected, 1: Male, 2: Female)
 * @param phoneNumber: String (Phone number of the user)
 * @param role: Int (Role of the user - 0: Not selected, 1: Trainer, 2: Trainee)
 * @param email: String (Email of the user)
 * @param bio: String (Biography of the user)
 * @param fcmToken: String (Firebase Cloud Messaging token of the user)
 * @param profileImageUrl: String (URL of the profile image of the user)
 * @param location: GeoPoint? (Location of the user)
 * @param isSetupDone: Boolean (Is the setup done for the user)
 */

data class User(
    val userId: String = "",
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
    val trainer: Trainer = Trainer(),

    //Trainer
    /*val certifications: List<Int> = emptyList(), //Külön dataclass trainer-trainee
    val areasOfExperties: List<Int> = emptyList(),
    val trainingStyles: List<Int> = emptyList(),
    val experience: Int = 0,
    val availability: Map<String, List<Int>> = emptyMap(),
    val pricing: Int = 0,
    val rating: Float = 0f,
    val specializations: List<Int> = emptyList(),
    val socialMediaLinks: Map<String, String> = emptyMap(),*/

    //trainee
    /*val unit: Int = 0,
    val height: Int = 0,
    val weight: Double = 0.0,
    val trainings: Int = 0,
    val fitnessGoals: List<Int> = emptyList(),
    val currentFitnessLevel: Int = 0, //Vagy int?
    val trainingPreference: Int = 0,
    val healthInformation: List<String> = emptyList(),
    val dietaryPreferences: List<String> = emptyList(),
    val availableEquipment: List<String> = emptyList(),*/

    val trainee: Trainee = Trainee(),

    val isSetupDone: Boolean = false,
    val isAdmin: Boolean = false
)

/**
 * @param certifications: List<String> (Certifications of the trainer)
 * @param areasOfExperties: List<String> (Areas of experties of the trainer)
 * @param trainingStyles: List<String> (Training styles of the trainer)
 * @param experience: Int (Experience of the trainer)
 * @param availability: Map<String, List<Int>> (Availability of the trainer)
 * @param pricing: Int (Pricing of the trainer)
 * @param rating: Float (Rating of the trainer)
 * @param specializations: List<String> (Specializations of the trainer)
 * @param socialMediaLinks: Map<String, String> (Social media links of the trainer)
 * @param pendingRequests: List<String> (Pending requests of the trainer)
 * @param acceptedRequests: List<String> (Accepted requests of the trainer)
 */
data class Trainer(
    val certifications: List<Int> = emptyList(),
    val areasOfExperties: List<Int> = emptyList(),
    val trainingStyles: List<Int> = emptyList(),
    val experience: Int = 0,
    val availability: Map<String, List<Int>> = emptyMap(),
    val pricing: Int = 0,
    val rating: Int = 0,
    val specializations: List<Int> = emptyList(),
    //val socialMediaLinks: Map<String, String> = emptyMap(),
    val socialMediaLinks: Map<String, String> = mapOf(
        "Facebook" to "",
        "Instagram" to "",
        "LinkedIn" to "",
        "X" to "",
        "YouTube" to "",
        "TikTok" to "",
        "Pinterest" to "",
        "Twitch" to "",
    ),
    val pendingRequests: List<String> = emptyList(),
    val acceptedRequests: List<String> = emptyList(),
)

/**
 * @param unit: String (Unit of the trainee: Metric, Imperial)
 * @param height: Double (Height of the trainee)
 * @param weight: Double (Weight of the trainee)
 * @param trainings: Int (Number of trainings of the trainee)
 * @param fitnessGoals: List<String> (Fitness goals of the trainee)
 * @param currentFitnessLevel: String (Current fitness level of the trainee)
 * @param trainingPreference: Int (Training preference of the trainee)
 * @param healthInformation: List<String> (Health information of the trainee)
 * @param dietaryPreferences: List<String> (Dietary preferences of the trainee)
 * @param availableEquipment: List<String> (Available equipment of the trainee)
 * @param activeTrainers: List<String> (Active trainers of the trainee)
 */

data class Trainee(
    val unit: Int = 0,
    val height: Int = 0,
    val weight: Double = 0.0,
    val trainings: Int = 0,
    val fitnessGoals: List<Int> = emptyList(),
    val currentFitnessLevel: Int = 0, //Vagy int?
    val trainingPreference: Int = 0,
    val healthInformation: List<Int> = emptyList(),
    val dietaryPreferences: List<Int> = emptyList(),
    val availableEquipment: List<Int> = emptyList(),
    val activeTrainers: List<String> = emptyList(),
)

fun User.toFirestoreUser(): FirestoreUser {
    return FirestoreUser(
        uid = userId,
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
        setupDone = isSetupDone
    )
}


fun User.toFirestoreTrainer(): FirestoreTrainer {
    return FirestoreTrainer(
        uid = userId,
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
    )
}

fun User.toFirestoreTrainee(): FirestoreTrainee {
    return FirestoreTrainee(
        uid = userId,
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
    )
}