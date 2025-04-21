package com.example.szakdogafitness.feature.lifestyle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.data.stepcount.IStepCountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepCountViewModel @Inject constructor(
    private val repository: IStepCountRepository
): ViewModel() {

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps.asStateFlow()

    fun updateStepCount(newSteps: Int) {
        viewModelScope.launch {
            repository.saveStepsLocally(newSteps)
            _steps.value = repository.getSteps()
        }
    }

    fun loadSteps() {
        viewModelScope.launch {
            _steps.value = repository.getSteps()
        }
    }

    fun syncSteps() {
        viewModelScope.launch {
            repository.syncStepsToFirestore()
        }
    }

    fun resetOnLogout() {
        viewModelScope.launch {
            repository.syncStepsToFirestore()
            repository.resetSteps()
        }
    }
}