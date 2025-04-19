package com.example.szakdogafitness

import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.szakdogafitness.presentation.navigation.NewRootNavigation
import com.example.szakdogafitness.ui.theme.SzakdogaFitnessTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val networkRequest = NetworkRequest.Builder().build()
        val networkOnState = mutableStateOf(false)
        connectivityManager.registerNetworkCallback(networkRequest, object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                networkOnState.value = true
                Log.d("NetworkCallback", "Network is available")
            }

            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                networkOnState.value = false
                Log.d("NetworkCallback", "Network is lost")
            }
        })

        enableEdgeToEdge()
        setContent {
            SzakdogaFitnessTheme {
                Scaffold( //UI might be good with column and the system bars, but it bugs the double scaffold at profile screen
                    contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) { innerPadding ->
                    val navController = rememberNavController()
                    NewRootNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        networkOn = networkOnState.value
                    )
                }
            }
        }
    }
}
