package com.example.szakdogafitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.szakdogafitness.domain.usecases.root.AllRootNavUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Deprecated("RootNavVM deprecated - REMOVE")
@HiltViewModel
class RootNavViewModel @Inject constructor(
    private val rootNavUseCase: AllRootNavUseCases
) : ViewModel(){

    private val _state = MutableStateFlow(RootNavState())
    val state = _state.asStateFlow()

    fun onEvent(event: RootNavEvent){
        when(event){
            is RootNavEvent.NavController -> {
                _state.value = state.value.copy(navController = event.navController)
            }
            is RootNavEvent.Navigate -> {
                rootNavUseCase.rootNavigate(
                    event.isLoggedIn,
                    event.isSetupDone,
                    event.isProfileNull,
                    state.value.navController!! //navController is not null
                )
            }
        }
    }
}


sealed class RootNavEvent{
    data class NavController(val navController: NavHostController): RootNavEvent()
    data class Navigate (
        val isLoggedIn: Boolean,
        val isSetupDone: Boolean,
        val isProfileNull: Boolean,
    ) : RootNavEvent()
}

data class RootNavState(
    var navController: NavHostController? = null
)