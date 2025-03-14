package com.example.szakdogafitness.domain.model

data class Nutrition (
    val foodId: Int = 0,
    val foodName: String = "",
    val servingSize: Int = 0,
    val calories: Int = 0,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fat: Int = 0,
    val image: String = "",
    val mealType: String = "" // Added field
)