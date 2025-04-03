package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import java.time.DayOfWeek

class AddEventToCalendarUseCase(private val repository: IMainService) {
    suspend operator fun invoke(events: Map<String, List<Int>>, title: String, description: String) {
        repository.addEventToCalendar(events, title, description)
    }
}