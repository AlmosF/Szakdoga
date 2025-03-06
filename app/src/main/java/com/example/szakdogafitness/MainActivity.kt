package com.example.szakdogafitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.szakdogafitness.feature.auth.AuthenticateScreen
import com.example.szakdogafitness.feature.lifestyle.LifeStyleScreen
import com.example.szakdogafitness.feature.nutrition.NutritionScreen
import com.example.szakdogafitness.feature.nutrition.SearchNutritionScreen
import com.example.szakdogafitness.feature.setup.SetupScreenBox
import com.example.szakdogafitness.feature.trainer.TrainerDetailsScreen
import com.example.szakdogafitness.presentation.navigation.root.RootNavigation
import com.example.szakdogafitness.ui.common.TrainerDemo
import com.example.szakdogafitness.ui.common.lifestyle.DailyCaloriesBlock
import com.example.szakdogafitness.ui.common.nutrition.TargetKcalBlock
import com.example.szakdogafitness.ui.common.nutrition.logblock.LogBlock
import com.example.szakdogafitness.ui.theme.SzakdogaFitnessTheme
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SzakdogaFitnessTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) { innerPadding ->
                    //SetupScreen(innerPadding)
                    //TraineeOne(innerPadding)
                    //TraineeTwo(innerPadding)
                    //TrainerOne(innerPadding)
                    //TrainerTwo(innerPadding)



                    //### WORKING SETUP ###
                    //LifeStyleScreen()
                    //### WORKING SETUP ###


                    //### WORKING SETUP ###
                    //SetupScreenBox(innerPadding)
                    //### WORKING SETUP ###


                    //### MEALLOGBLOCK ###
                    /*LogBlock(
                        modifier = Modifier.padding(innerPadding)
                    )*/
                    //### MEALLOGBLOCK ###




                    //### NUTRITIONSCREEN ###
                    /*Column (
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        /*AddNutrientsBlock(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth(0.9f)
                        )*/
                        var kcal = 1500
                        var targetKcal by remember { mutableStateOf(1500) }
                        var maxKcal by remember { mutableStateOf(2500) }
                        TargetKcalBlock(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxWidth(0.9f),
                            kcal = kcal,
                            targetKcal = targetKcal,
                            maxKcal = maxKcal,
                            onIncrease = {
                                targetKcal += it
                            },
                            onDecrease = {
                                targetKcal -= it
                            }

                        )
                    }*/

                    /*NutritionScreen(
                        modifier = Modifier.padding(innerPadding)
                    )*/
                    //### NUTRITIONSCREEN ###


                    /*SearchNutritionScreen(
                        modifier = Modifier.padding(innerPadding)
                    )*/


                    //### WORKING ROOT NAV ###
                    val navController = rememberNavController()
                    RootNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                    )
                    //### WORKING ROOT NAV ###



                    //DailyCaloriesBlock()

                    /*AuthenticateScreen(
                        modifier = Modifier.padding(innerPadding)
                    )*/
                    /*val rootNavHostController = rememberNavController()
                    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
                    RootNavigation(
                        modifier = Modifier.padding(innerPadding),
                        rootNavController = rootNavHostController,
                        authViewModel = authenticationViewModel
                    )*/
                    //SetupScreenBox(innerPadding)
                    //#### TrainerCard

                    /*LazyColumn (
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        items(11){
                            TrainerCard(
                                data = TrainerDemo(
                                    Fname = it.toString(),
                                    Lname = "Trainer",
                                    specializations = listOf("Specialization1", "Specialization2"),
                                    image = "image",
                                    rating = it,
                                    price = 100,
                                    distance = 5.0f,
                                    gender = if (it % 2 == 0) "male" else "female"
                                )
                            )
                        }
                    }*/


                    //#############

                    /*var selectedHours by remember { mutableStateOf(emptyMap<String, List<Int>>()) }

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        TrainerTimetableView(
                            selectedHours = selectedHours,
                            onHourSelected = { day, hour ->
                                selectedHours = selectedHours.toMutableMap().apply {
                                    val hours = getOrDefault(day, emptyList()).toMutableList()
                                    if (hours.contains(hour)) {
                                        hours.remove(hour)
                                        if (hours.isEmpty()) {
                                            remove(day)
                                        } else {
                                            put(day, hours)
                                        }
                                    } else {
                                        hours.add(hour)
                                        put(day, hours)
                                    }
                                }
                            }
                        )

                        selectedHours.forEach { (day, hours) ->
                            Text("$day: ${hours.joinToString()}")
                        }

                        var selectedHoursTest by remember { mutableStateOf(emptyMap<String, List<Int>>()) }

                        Spacer(modifier = Modifier.height(16.dp)) // Ensure separation

                        TraineeTimetableView(
                            selectedHours = selectedHoursTest,
                            availableHours = selectedHours,
                            onHourSelected = { day, hour ->
                                selectedHoursTest = selectedHoursTest.toMutableMap().apply {
                                    val hours = getOrDefault(day, emptyList()).toMutableList()
                                    if (hours.contains(hour)) {
                                        hours.remove(hour)
                                        if (hours.isEmpty()) {
                                            remove(day)
                                        } else {
                                            put(day, hours)
                                        }
                                    } else {
                                        hours.add(hour)
                                        put(day, hours)
                                    }
                                }
                            }
                        )

                        selectedHoursTest.forEach { (day, hours) ->
                            Text("$day: ${hours.joinToString()}")
                        }
                        LazyColumn {
                            items(5){
                                TrainerCard(
                                    data = TrainerDemo(
                                        Fname = it.toString(),
                                        Lname = "Trainer",
                                        specializations = listOf("Specialization1", "Specialization2"),
                                        image = "image",
                                        rating = 5,
                                        price = 100,
                                        distance = 5.0f,
                                        gender = if (it % 2 == 0) "male" else "female"
                                    )
                                )
                            }
                        }


                    /*TimetableView(
                        selectedHours = selectedHours,
                        onHourSelected = { day, hour ->
                            selectedHours = selectedHours.toMutableMap().apply {
                                val hours = getOrDefault(day, emptyList()).toMutableList()
                                if (hours.contains(hour)) {
                                    hours.remove(hour)
                                } else {
                                    hours.add(hour)
                                }
                                put(day, hours)
                            }
                        }
                    )

                    // Display selected hours for debugging
                    selectedHours.forEach { (day, hours) ->
                        Text("$day: ${hours.joinToString()}")
                    }
                    Log.d("SelectedHours", selectedHours.toString())
                    // Second TimetableView (Uses selectedHours to enable buttons)
                    var selectedHoursTest by remember { mutableStateOf(emptyMap<String, List<Int>>()) }
                    TimetableView(
                        selectedHours = selectedHours,  // Use the same state as above
                        onHourSelected = { day, hour ->
                            selectedHoursTest = selectedHoursTest.toMutableMap().apply {
                                val hours = getOrDefault(day, emptyList()).toMutableList()
                                if (hours.contains(hour)) {
                                    hours.remove(hour)
                                } else {
                                    hours.add(hour)
                                }
                                put(day, hours)
                            }

                        },   // No need to handle selection in the second view
                        onTrainee = true,
                        onTraineeSelectedHours = selectedHoursTest
                    )

                    selectedHoursTest.forEach { (day, hours) ->
                        Text("$day: ${hours.joinToString()}")
                    }*/

                    }*/
                }
            }
        }
    }
}
