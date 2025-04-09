package com.example.szakdogafitness.domain.model

import com.google.firebase.Timestamp

data class StepCount(
    val userId: String = "",
    val steps: Int = 0,
    val date: Timestamp = Timestamp.now(),
)