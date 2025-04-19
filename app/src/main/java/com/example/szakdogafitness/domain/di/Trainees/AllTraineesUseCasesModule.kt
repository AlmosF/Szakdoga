package com.example.szakdogafitness.domain.di.Trainees

import com.example.szakdogafitness.data.trainees.ITraineesService
import com.example.szakdogafitness.domain.usecases.trainees.AcceptRequestUseCase
import com.example.szakdogafitness.domain.usecases.trainees.AddEventToCalendarUseCase
import com.example.szakdogafitness.domain.usecases.trainees.AllTraineesUseCases
import com.example.szakdogafitness.domain.usecases.trainees.DeleteRequestUseCase
import com.example.szakdogafitness.domain.usecases.trainees.FetchRequestsUseCase
import com.example.szakdogafitness.domain.usecases.trainees.FetchUsersDailyDatasByIdUseCase
import com.example.szakdogafitness.domain.usecases.trainees.RemoveEventFromCalendarUseCase
import com.example.szakdogafitness.domain.usecases.trainees.RemoveTraineeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllTraineesUseCasesModule {


    @Provides
    @Singleton
    fun provideFetchUsersDailyDatasByIdUseCase(repository: ITraineesService): FetchUsersDailyDatasByIdUseCase =
        FetchUsersDailyDatasByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteRequestUseCase(repository: ITraineesService): DeleteRequestUseCase =
        DeleteRequestUseCase(repository)

    @Provides
    @Singleton
    fun provideAcceptRequestUseCase(repository: ITraineesService): AcceptRequestUseCase =
        AcceptRequestUseCase(repository)

    @Provides
    @Singleton
    fun provideRemoveTraineeUseCase(repository: ITraineesService): RemoveTraineeUseCase =
        RemoveTraineeUseCase(repository)

    @Provides
    @Singleton
    fun provideAddEventToCalendarUseCase(repository: ITraineesService): AddEventToCalendarUseCase =
        AddEventToCalendarUseCase(repository)

    @Provides
    @Singleton
    fun provideRemoveEventFromCalendarUseCase(repository: ITraineesService): RemoveEventFromCalendarUseCase =
        RemoveEventFromCalendarUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchRequestsUseCase(repository: ITraineesService): FetchRequestsUseCase =
        FetchRequestsUseCase(repository)

    @Provides
    @Singleton
    fun provideAllTraineesUseCases(
        repository: ITraineesService,
        acceptRequest: AcceptRequestUseCase,
        addEventToCalendar: AddEventToCalendarUseCase,
        removeEventFromCalendar: RemoveEventFromCalendarUseCase,
        deleteRequest: DeleteRequestUseCase,
        removeTrainee: RemoveTraineeUseCase,
        fetchRequests: FetchRequestsUseCase,
        fetchUsersDailyDatasById: FetchUsersDailyDatasByIdUseCase,
    ): AllTraineesUseCases = AllTraineesUseCases(
        repository,
        acceptRequest,
        addEventToCalendar,
        removeEventFromCalendar,
        deleteRequest,
        removeTrainee,
        fetchRequests,
        fetchUsersDailyDatasById
    )

}