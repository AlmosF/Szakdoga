package com.example.szakdogafitness.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.szakdogafitness.domain.usecases.appbar.AllAppBarUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor(
    private val appBarUseCases: AllAppBarUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AppBarState())
    val state = _state.asStateFlow()

    fun onEvent(event: AppBarEvent){
        when(event){
            is AppBarEvent.BottomBarComposableChanged -> {
                _state.update { it.copy(bottomBarComposable = event.bottomBarComposable) }
            }
            is AppBarEvent.BottomBarVisibilityChanged -> {
                _state.update { it.copy(bottomBarVisibility = event.bottomBarVisibility) }
            }
            is AppBarEvent.TopBarComposableChanged -> {
                //_state.update { it.copy(topBarComposable = event.topBarComposable) }
                _state.value = _state.value.copy(topBarComposable = event.topBarComposable)
            }
            is AppBarEvent.TopBarVisibilityChanged -> {
                _state.update { it.copy(topBarVisibility = event.topBarVisibility) }
            }
            is AppBarEvent.CurrentNavControllerChanged -> {
                _state.update { it.copy(currentNavController = event.navController) }
            }

            is AppBarEvent.AppBarVisibility -> {
                val visibility = appBarUseCases.appBarVisibilityUseCase(event.screen)
                _state.update {
                    it.copy(
                        topBarVisibility = visibility.first,
                        bottomBarVisibility = visibility.second
                    )
                }
            }
        }
    }
}


data class AppBarState(
    val currentNavController: NavHostController? = null,
    val topBarComposable: @Composable () -> Unit = {},
    val bottomBarComposable: @Composable () -> Unit = {},
    val topBarVisibility: Boolean = true,
    val bottomBarVisibility: Boolean = true
)


sealed class AppBarEvent{
    data class AppBarVisibility(val screen: String): AppBarEvent()
    data class CurrentNavControllerChanged(val navController: NavHostController): AppBarEvent()
    data class TopBarComposableChanged(val topBarComposable: @Composable () -> Unit): AppBarEvent()
    data class BottomBarComposableChanged(val bottomBarComposable: @Composable () -> Unit): AppBarEvent()
    data class TopBarVisibilityChanged(val topBarVisibility: Boolean): AppBarEvent()
    data class BottomBarVisibilityChanged(val bottomBarVisibility: Boolean): AppBarEvent()
}
