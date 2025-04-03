package com.example.szakdogafitness.domain.model

import com.google.firebase.Timestamp


data class DailyDatas(
    val uid: String = "",
    //val dailyData: List<DailyData> = listOf() //fixme("Workingone")
)

data class DailyData(
    val date: Timestamp = Timestamp.now(),
    val waterIntake: Double = 0.0,
    val targetWater: Double = 2.0,
    val steps: Int = 0,
    val targetSteps: Int = 6000,
    val foodIntake: Map<String, List<Nutrition>> = mapOf(),
    val foodIntakeM: Map<String, List<Int>> = mapOf(),
    val targetKcal: Int = 1700,
    val manualFoodIntake: Map<String, List<Nutrition>> = mapOf(),
    val manualFoodIntakeM: Map<String, List<Int>> = mapOf(),
)