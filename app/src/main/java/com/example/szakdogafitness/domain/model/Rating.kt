package com.example.szakdogafitness.domain.model

import com.google.firebase.Timestamp

data class Rating(
    val rating: Int = 0,
    val timestamp: Timestamp = Timestamp.now()
)
