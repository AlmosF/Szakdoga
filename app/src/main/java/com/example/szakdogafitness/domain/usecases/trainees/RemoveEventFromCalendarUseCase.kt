package com.example.szakdogafitness.domain.usecases.trainees

import com.example.szakdogafitness.data.trainees.ITraineesService

/*
class RemoveEventFromCalendarUseCase(private val repository: IMainService){
    suspend operator fun invoke(events: Map<String, List<Int>>, title: String, description: String) {
        repository.removeEventFromCalendar(events, title, description)
    }
}*/
class RemoveEventFromCalendarUseCase(private val repository: ITraineesService){
    suspend operator fun invoke(events: Map<String, List<Int>>, title: String, description: String) {
        repository.removeEventFromCalendar(events, title, description)
    }
}