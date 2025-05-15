package com.example.szakdogafitness.domain.usecases.trainees

import com.example.szakdogafitness.data.trainees.ITraineesService

class AddEventToCalendarUseCase(private val repository: ITraineesService) {
    suspend operator fun invoke(events: Map<String, List<Int>>, title: String, description: String) {
        repository.addEventToCalendar(events, title, description)
    }
}