package com.example.szakdogafitness.domain.model

import com.google.firebase.Timestamp

data class Requests(
    val appointment: Map<String, List<Int>> = mapOf(),
    val createdAt: Timestamp = Timestamp.now(),
    val status: String = "pending",
)