package com.example.szakdogafitness.domain.model

import com.google.firebase.Timestamp


data class DailyDatas(
    val userId: String = "",
    val dailyData: List<DailyData> = listOf()
)

data class DailyData(
    val date: Timestamp = Timestamp.now(),
    val waterIntake: Double = 0.0,
    val targetWater: Double = 0.0,
    val steps: Int = 0,
    val targetSteps: Int = 0,
    val foodIntake: Map<String, List<Nutrition>> = mapOf(),
    val targetKcal: Int = 0,
    val manualFoodIntake: Map<String, List<Nutrition>> = mapOf(),
)