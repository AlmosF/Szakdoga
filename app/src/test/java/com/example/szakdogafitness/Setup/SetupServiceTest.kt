package com.example.szakdogafitness.Setup

import androidx.compose.runtime.currentComposer
import com.example.szakdogafitness.data.setup.ISetupService
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.viewmodel.SetupProfileState
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import org.junit.Before
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue


class SetupServiceTest {

    private val setupService = mockk<ISetupService>()


    @Test
    fun isPreviousButtonVisibleAtFirstScreen(){
        every { setupService.previousButtonVisible(0) } returns true
        val isVisible = setupService.previousButtonVisible(0)
        assertTrue(isVisible)
    }

    @Test
    fun isNextButtonEnabledTest(){
        val state = SetupProfileState(
            isLoading = false,
            user = User(),
            currentScreen = 0,
            barStatus = 0.0,
        )
        every { setupService.nextButtonEnabled(state) } returns false
        val enabled = setupService.nextButtonEnabled(state)
        assertFalse(enabled)
    }

}