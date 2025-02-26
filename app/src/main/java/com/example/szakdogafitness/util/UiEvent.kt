package com.example.szakdogafitness.util

import com.example.szakdogafitness.ui.model.UiText

sealed class UiEvent {
    data class Success(val message: UiText? = null): UiEvent()

    data class Failure(val message: UiText): UiEvent()
}
