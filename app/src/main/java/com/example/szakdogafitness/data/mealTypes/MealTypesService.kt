package com.example.szakdogafitness.data.mealTypes

import com.example.szakdogafitness.domain.model.FirestoreMealtypes
import com.example.szakdogafitness.domain.model.Nutrition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MealTypesService @Inject constructor(
    private val firestore: FirebaseFirestore
) : IMealTypesService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMealTypes(): Flow<Map<String, List<Nutrition>>> = flow {
        val mealTypesDoc = listOf("breakfast", "morning_snack", "lunch", "afternoon_snack", "dinner", "evening_snack")

        val mealTypes = mealTypesDoc.map { mealType ->
            firestore.collection("mealtype")
                .document(mealType)
                .dataObjects<FirestoreMealtypes>()
                .map { mealTypesList ->
                    mealTypesList?.foods?.let { mealType to it } ?: (mealType to emptyList())
                }
        }

        val combinedMealTypes = combine(mealTypes) { mealTypePairs ->
            mealTypePairs.toMap()
        }.flatMapLatest { flowOf(it) }

        emitAll(combinedMealTypes)

    }.flowOn(Dispatchers.IO)

    override fun getNutritions(): Flow<List<Nutrition>> =
        firestore.collection("nutritions")
            .dataObjects<Nutrition>()

    override fun getNutritionById(id: String): Flow<Nutrition?> =
        firestore.collection("nutritions")
            .document(id)
            .dataObjects<Nutrition>()

    override suspend fun addNutrition(nutrition: Nutrition) {
        firestore.collection("nutritions")
            .document(nutrition.foodId.toString())
            .set(nutrition)
            .await()
    }

    override suspend fun updateNutrition(nutrition: Nutrition) {
        firestore.collection("nutritions")
            .document(nutrition.foodId.toString())
            .set(nutrition)
            .await()
    }

}