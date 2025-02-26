package com.example.szakdogafitness.domain.model

data class Nutrition (
    val foodId: Int,
    val foodName: String,
    val servingSize: Int,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val image: String
)