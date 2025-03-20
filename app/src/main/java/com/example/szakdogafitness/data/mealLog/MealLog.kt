package com.example.szakdogafitness.data.mealLog

import android.util.Log
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.toMutableList

class MealLog @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase
) : IMealLog {
    override suspend fun consumeMeal(
        mealID: Int,
        mealType: String,
        date: Timestamp
    ) {
        val userId = userIdUseCase()
        val dateFormated = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.toDate())
        try {
            val dailyDataRef = firestore.collection("dailyDatas")
                .document(userId!!)
                .collection("dailyData")
                .document(dateFormated)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(dailyDataRef)

                val foodIntakeM = snapshot.get("foodIntakeM") as? MutableMap<String, MutableList<Int>> ?: mutableMapOf()

                val mealList = foodIntakeM[mealType]?.toMutableList() ?: mutableListOf()

                mealList.add(mealID)

                foodIntakeM[mealType] = mealList

                transaction.update(dailyDataRef, "foodIntakeM", foodIntakeM)

            }
            Log.d("Firestore", "Meal $mealID added to $mealType successfully.")
        } catch (e: Exception) {
            Log.e("Firestore", "Error updating meal data: ${e.message}", e)
        }
    }

    override suspend fun replenishMeal(
        mealID: Int,
        mealType: String,
        date: Timestamp
    ) {
        val userId = userIdUseCase()
        val dateFormatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.toDate())
        try {
            val dailyDataRef = firestore.collection("dailyDatas")
                .document(userId!!)
                .collection("dailyData")
                .document(dateFormatted)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(dailyDataRef)

                val foodIntakeM = snapshot.get("foodIntakeM") as? Map<String, List<Long>> ?: emptyMap()

                val mutableFoodIntakeM = foodIntakeM.toMutableMap()

                val mealList = mutableFoodIntakeM[mealType]?.toMutableList() ?: mutableListOf()

                mealList.remove(mealID.toLong())

                if (mealList.isEmpty()) {
                    mutableFoodIntakeM.remove(mealType)
                } else {
                    mutableFoodIntakeM[mealType] = mealList
                }

                transaction.update(dailyDataRef, "foodIntakeM", mutableFoodIntakeM)



            }
            Log.d("Firestore", "Meal $mealID removed from $mealType successfully, for date $dateFormatted, user $userId.")
        } catch (e: Exception) {
            Log.e("Firestore", "Error updating meal data: ${e.message}", e)
        }
    }
    /*override fun consumeMeal(
        mealType: String,
        meal: Nutrition,
        dataMap: Map<String, List<Nutrition>>
    ): Map<String, List<Nutrition>> {
        val newConsumedMeals = dataMap.toMutableMap()
        val mealList = newConsumedMeals[mealType]?.toMutableList() ?: mutableListOf()
        mealList.add(meal)
        newConsumedMeals[mealType] = mealList
        return newConsumedMeals
    }*/


    override fun removeMeal(
        mealType: String,
        index: Int,
        dataMap: Map<String, List<Nutrition>>
    ): Map<String, List<Nutrition>> {
        val newConsumedMeals = dataMap.toMutableMap()
        newConsumedMeals[mealType]?.let {
            val newMealList = it.toMutableList()
            newMealList.removeAt(index)
            newConsumedMeals[mealType] = newMealList
        }
        return newConsumedMeals
    }

}