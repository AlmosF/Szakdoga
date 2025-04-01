package com.example.szakdogafitness.domain.model

data class FoodAnalysisResult(
    val foodName: String = "",
    val kcal: Int = 0,
    val fat: Int = 0,
    val carb: Int = 0,
    val protein: Int = 0,
    val servingSize: Int = 0,
)