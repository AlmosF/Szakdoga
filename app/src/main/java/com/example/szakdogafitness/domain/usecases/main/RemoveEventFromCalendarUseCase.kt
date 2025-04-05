package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class RemoveEventFromCalendarUseCase(private val repository: IMainService){

    suspend operator fun invoke(events: Map<String, List<Int>>, title: String, description: String) {
        repository.removeEventFromCalendar(events, title, description)
    }

}