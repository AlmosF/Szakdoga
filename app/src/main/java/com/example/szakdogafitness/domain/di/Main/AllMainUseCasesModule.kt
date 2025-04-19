package com.example.szakdogafitness.domain.di.Main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.domain.usecases.dailyDatas.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.main.AllMainUseCases
import com.example.szakdogafitness.domain.usecases.trainers.ApplyToTrainerAppointmentUseCase
import com.example.szakdogafitness.domain.usecases.trainers.FetchRequestedAppointmentsUseCase
import com.example.szakdogafitness.domain.usecases.trainers.FetchTrainersRequestsUseCase
import com.example.szakdogafitness.domain.usecases.profile.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.GenerateNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.trainers.GetTrainersUseCase
import com.example.szakdogafitness.domain.usecases.profile.UpdateUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllMainUseCasesModule {


    /*@Provides
    @Singleton
    fun provideFetchUserUseCase(repository: IMainService): FetchUserUseCase =
        FetchUserUseCase(repository)*/

    /*@Provides
    @Singleton
    fun provideFetchUsersDailyDatasUseCase(repository: IMainService): FetchUsersDailyDatasUseCase =
        FetchUsersDailyDatasUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideGenerateNewDailyDataUseCase(repository: IMainService): GenerateNewDailyDataUseCase =
        GenerateNewDailyDataUseCase(repository)
*/
    /*@Provides
    @Singleton
    fun provideGetTrainersUseCase(repository: IMainService): GetTrainersUseCase =
        GetTrainersUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideUpdateUserUseCase(repository: IMainService): UpdateUserUseCase =
        UpdateUserUseCase(repository)*/

    /*@Singleton
    @Provides
    fun provideApplyToTrainerAppointmentUseCase(repository: IMainService): ApplyToTrainerAppointmentUseCase =
        ApplyToTrainerAppointmentUseCase(repository)*/

    /*@Singleton
    @Provides
    fun provideFetchTrainersRequestsUseCase(repository: IMainService): FetchTrainersRequestsUseCase =
        FetchTrainersRequestsUseCase(repository)
    */

    @Singleton
    @Provides
    fun provideFetchRequestedAppointmentsUseCase(repository: IMainService): FetchRequestedAppointmentsUseCase =
        FetchRequestedAppointmentsUseCase(repository)

    /*@Provides
    @Singleton
    fun provideFetchRequestedAppoint(repository: IMainService): FetchRequestedAppoint =
        FetchRequestedAppoint(repository)
    */

    /*@Provides
    @Singleton
    fun provideFetchRequestsUseCase(repository: IMainService): FetchRequestsUseCase =
        FetchRequestsUseCase(repository)*/

    /*@Provides
    @Singleton
    fun provideDeleteRequestUseCase(repository: IMainService): DeleteRequestUseCase =
        DeleteRequestUseCase(repository)*/

    /*@Provides
    @Singleton
    fun provideAcceptRequestUseCase(repository: IMainService): AcceptRequestUseCase =
        AcceptRequestUseCase(repository)*/

    /*@Provides
    @Singleton
    fun provideAcceptedTrainersUseCase(repository: IMainService): GetAcceptedTrainersUseCase =
        GetAcceptedTrainersUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideRateTrainerUseCase(repository: IMainService): RateTrainerUseCase =
        RateTrainerUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideLoadTrainersRateUseCase(repository: IMainService): LoadTrainersRateUseCase =
        LoadTrainersRateUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideLoadAllTrainersAllRateUseCase(repository: IMainService): LoadAllTrainersAllRateUseCase =
        LoadAllTrainersAllRateUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideFetchUsersDailyDatasByIdUseCase(repository: IMainService): FetchUsersDailyDatasByIdUseCase =
        FetchUsersDailyDatasByIdUseCase(repository)
*/
    /*@Provides
    @Singleton
    fun provideTargetKcalModifyUseCase(repository: IMainService): TargetKcalModifyUseCase =
        TargetKcalModifyUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideTargetWaterModifyUseCase(repository: IMainService): TargetWaterModifyUseCase =
        TargetWaterModifyUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideUpdateWaterIntakeUseCase(repository: IMainService): WaterIntakeModifyUseCase =
        WaterIntakeModifyUseCase(repository)
    */

    /*@Provides
    @Singleton
    fun provideAddEventToCalendarUseCase(repository: IMainService): AddEventToCalendarUseCase {
        return AddEventToCalendarUseCase(repository)
    }*/

    /*@Provides
    @Singleton
    fun provideRemoveEventFromCalendarUseCase(repository: IMainService): RemoveEventFromCalendarUseCase {
        return RemoveEventFromCalendarUseCase(repository)
    }*/

    /*@Provides
    @Singleton
    fun provideRemoveTraineeUseCase(repository: IMainService): RemoveTraineeUseCase =
        RemoveTraineeUseCase(repository)*/

    /*@Provides
    @Singleton
    fun provideUploadProfilePictureUseCase(repository: IMainService): UploadProfilePictureUseCase =
        UploadProfilePictureUseCase(repository)
*/
    @Provides
    @Singleton
    fun provideMainUseCases(
        repository: IMainService,
        fetchUserUseCase: FetchUserUseCase,
        fetchUsersDailyDatasUseCase: FetchUsersDailyDatasUseCase,
        generateNewDailyDataUseCase: GenerateNewDailyDataUseCase,
        getTrainersUseCase: GetTrainersUseCase,
        updateUserUseCase: UpdateUserUseCase,
        applyToTrainerAppointmentUseCase: ApplyToTrainerAppointmentUseCase,
        fetchTrainersRequests: FetchTrainersRequestsUseCase,
        fetchRequestedAppointments: FetchRequestedAppointmentsUseCase
    ): AllMainUseCases = AllMainUseCases(
        repository,
        fetchUserUseCase,
        fetchUsersDailyDatasUseCase,
        generateNewDailyDataUseCase,
        getTrainersUseCase,
        updateUserUseCase,
        applyToTrainerAppointmentUseCase,
        fetchTrainersRequests,
        fetchRequestedAppointments
    )


}