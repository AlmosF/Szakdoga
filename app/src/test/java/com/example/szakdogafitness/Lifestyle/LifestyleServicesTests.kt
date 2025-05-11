package com.example.szakdogafitness.Lifestyle

import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.example.szakdogafitness.data.mealLog.IMealLog
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.usecases.dailyDatas.AllDailyDatasUseCases
import com.example.szakdogafitness.domain.usecases.dailyDatas.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.GenerateNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.TargetKcalModifyUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.TargetWaterModifyUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.WaterIntakeModifyUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.MealConsumedUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.MealRemovedUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.MealReplenishedUseCase
import com.example.szakdogafitness.feature.lifestyle.DailyDatasEvent
import com.example.szakdogafitness.feature.lifestyle.DailyDatasState
import com.example.szakdogafitness.feature.lifestyle.DailyDatasViewModel
import com.google.firebase.Timestamp
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.times
import java.util.Date
import kotlin.test.AfterTest
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LifestyleServicesTests{

    private lateinit var viewModel: DailyDatasViewModel

    private val dRepository = mockk<IDailyDatasService>()
    private val fetchUsersDailyDatas = mockk<FetchUsersDailyDatasUseCase>(relaxed = true)
    private val generateNewDailyData = mockk<GenerateNewDailyDataUseCase>(relaxed = true)
    private val targetKcalModify = mockk<TargetKcalModifyUseCase>(relaxed = true)
    private val targetWaterModify = mockk<TargetWaterModifyUseCase>(relaxed = true)
    private val waterIntakeModify = mockk<WaterIntakeModifyUseCase>(relaxed = true)

    private val mRepository = mockk<IMealLog>()
    private val mealConsumed = mockk<MealConsumedUseCase>(relaxed = true)
    private val mealRemoved = mockk<MealRemovedUseCase>(relaxed = true)
    private val mealReplenished = mockk<MealReplenishedUseCase>(relaxed = true)

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        val allDailyDatasUseCases = AllDailyDatasUseCases(
            dRepository,
            fetchUsersDailyDatas,
            generateNewDailyData,
            targetKcalModify,
            targetWaterModify,
            waterIntakeModify
        )

        val allMealLogUseCases = com.example.szakdogafitness.domain.usecases.mealLog.AllMealLogUseCases(
            mRepository,
            mealConsumed,
            mealRemoved,
            mealReplenished
        )

        viewModel = DailyDatasViewModel(
            allDailyDatasUseCases,
            allMealLogUseCases
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun loadDailyDatasAndAssertEqualsTests() = runTest {
        val dailyDataList = listOf(
            DailyData(targetWater = 1.1),
            DailyData(targetWater = 1.2),
            DailyData(targetWater = 1.3),
        )
        coEvery { fetchUsersDailyDatas.invoke() } returns flowOf(dailyDataList)
        viewModel.onEvent(DailyDatasEvent.LoadDailyDatas)
        advanceUntilIdle()
        assertEquals(dailyDataList, viewModel.state.value.dailyDataList)
        assertEquals(1.1, viewModel.state.value.dailyDataList[0].targetWater)
        //Just Update
        viewModel.onEvent(DailyDatasEvent.LoadDailyDatas)
        advanceUntilIdle()
        assertEquals(dailyDataList, viewModel.state.value.dailyDataList)
    }

    @Test
    fun dayChangedAndAssertEqualsTests() = runTest {
        val dailyDataList = listOf(
            DailyData(targetWater = 1.1),
            DailyData(targetWater = 1.2),
            DailyData(targetWater = 1.3),
        )
        coEvery { fetchUsersDailyDatas.invoke() } returns flowOf(dailyDataList)
        viewModel.onEvent(DailyDatasEvent.LoadDailyDatas)
        advanceUntilIdle()
        assertEquals(dailyDataList, viewModel.state.value.dailyDataList)
        assertEquals(1.3, viewModel.state.value.currentDailyData.targetWater)
        //Just Update
        viewModel.onEvent(DailyDatasEvent.PreviousDay)
        advanceUntilIdle()
        assertEquals(dailyDataList[1], viewModel.state.value.currentDailyData)
        assertEquals(1.2, viewModel.state.value.currentDailyData.targetWater)
        viewModel.onEvent(DailyDatasEvent.NextDay)
        advanceUntilIdle()
        assertEquals(dailyDataList[2], viewModel.state.value.currentDailyData)
        assertEquals(1.3, viewModel.state.value.currentDailyData.targetWater)
        viewModel.onEvent(DailyDatasEvent.ResetDailyData)
        advanceUntilIdle()
        assertEquals(DailyDatasState(), viewModel.state.value)
    }

    @Test
    fun updateTargetsTests() = runTest {
        val dailyDataList = listOf(
            DailyData(targetWater = 1.1),
            DailyData(targetWater = 1.2),
            DailyData(targetWater = 1.3),
        )
        val timestamp = mockk<Timestamp>(relaxed = true)
        coEvery { fetchUsersDailyDatas.invoke() } returns flowOf(dailyDataList)
        viewModel.onEvent(DailyDatasEvent.LoadDailyDatas)
        advanceUntilIdle()
        coEvery { targetKcalModify.invoke(1000, timestamp) }
        coEvery { targetWaterModify.invoke(2.0, timestamp) }
        coEvery { waterIntakeModify.invoke(3.5, timestamp) }
        viewModel.onEvent(DailyDatasEvent.UpdateTargetKcal(1000, timestamp))
        viewModel.onEvent(DailyDatasEvent.UpdateTargetWater(2.0, timestamp))
        viewModel.onEvent(DailyDatasEvent.UpdateWaterIntake(3.5, timestamp))
        advanceUntilIdle()
        coVerify { targetKcalModify.invoke(1000, timestamp) }
        coVerify { targetWaterModify.invoke(2.0, timestamp) }
        coVerify { waterIntakeModify.invoke(3.5, timestamp) }
    }


}