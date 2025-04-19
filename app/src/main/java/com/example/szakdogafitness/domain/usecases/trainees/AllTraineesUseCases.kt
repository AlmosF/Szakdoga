package com.example.szakdogafitness.domain.usecases.trainees

import com.example.szakdogafitness.data.trainees.ITraineesService

class AllTraineesUseCases(
    val repository: ITraineesService,
    val acceptRequest: AcceptRequestUseCase,
    val addEventToCalendar: AddEventToCalendarUseCase,
    val removeEventFromCalendar: RemoveEventFromCalendarUseCase,
    val deleteRequest: DeleteRequestUseCase,
    val removeTrainee: RemoveTraineeUseCase,
    val fetchRequests: FetchRequestsUseCase,
    val fetchUsersDailyDatasById: FetchUsersDailyDatasByIdUseCase,
)