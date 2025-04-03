package com.example.szakdogafitness.feature.request

import android.R.attr.data
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.R
import com.example.szakdogafitness.feature.setup.toTriple
import com.example.szakdogafitness.feature.trainee.TraineeBioScreen
import com.example.szakdogafitness.ui.common.appbars.BackNavWithTitle
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.TraineesEvent
import com.example.szakdogafitness.viewmodel.TraineesViewModel
import com.google.common.collect.Multimaps.index

@Composable
fun RequestDetailScreen(
    modifier: Modifier = Modifier,
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    traineeId: String,
    onBackClick: () -> Unit = {},
) {
    val traineesState by traineesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        traineesViewModel.onEvent(TraineesEvent.AppointmentsToCalendar(false))
    }

    LaunchedEffect(traineeId, traineesState.pendingRequests) {
        traineesViewModel.onEvent(TraineesEvent.SelectTrainee(
            userId = traineeId
        ))
    }
    LaunchedEffect(traineesState.selectedTrainee) {
        traineesState.selectedTrainee?.let {
            appBarViewModel.onEvent(
                AppBarEvent.TopBarComposableChanged({
                        BackNavWithTitle(
                            title = "${it.second.firstName} ${it.second.lastName}",
                        ) {
                            onBackClick()
                        }
                    })
            )
        }
    }

    val painter = rememberAsyncImagePainter(traineesState.selectedTrainee?.second?.profileImageUrl)
    val painterState by painter.state.collectAsState()
    val context = LocalContext.current


    if (traineesState.isLoading) {
        CircularProgressIndicator()
    } else {
        Column (
            modifier = modifier
                .padding(top = 8.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (painterState) {
                        AsyncImagePainter.State.Empty -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(150.dp)
                            )
                        }
                        is AsyncImagePainter.State.Error -> {
                            Image(
                                painter = painterResource(id = android.R.drawable.ic_menu_report_image),
                                contentDescription = "Error",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(shape = RoundedCornerShape(16.dp))
                            )
                        }
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(150.dp)
                            )
                        }
                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = "Trainer",
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .size(150.dp)
                                ,
                                contentScale = ContentScale.Crop
                            )
                        }
                    } // FIXME: ERROR IMAGE REPLACE
                    val tripleDOB = traineesState.selectedTrainee?.second?.dateOfBirth?.toTriple()
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "${tripleDOB?.third}. ${tripleDOB?.second}. ${tripleDOB?.first}",
                        fontSize = 20.sp,
                    )
                }
                Column (
                    modifier = Modifier
                        .padding(8.dp)
                        .heightIn(150.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    val fitnessGoalsList = context.resources.getStringArray(R.array.fitness_goal).toList()
                    Text(
                        text = traineesState.selectedTrainee?.second?.trainee?.fitnessGoals?.joinToString(", ") { fitnessGoalsList[it] } ?: "",
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        fontSize = 16.sp
                    )
                    val fitnessLevelList = context.resources.getStringArray(R.array.current_fitness_level).toList()
                    Text(
                        text = traineesState.selectedTrainee?.second?.trainee?.currentFitnessLevel?.let { fitnessLevelList[it] } ?: "",
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        fontSize = 16.sp
                    )
                    val trainerPreferenceArray = context.resources.getStringArray(R.array.trainer_preference).toList()
                    Text(
                        text = traineesState.selectedTrainee?.second?.trainee?.trainingPreference?.let { trainerPreferenceArray[it] } ?: "",
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        fontSize = 16.sp
                    )
                    val availableEquipmentList = context.resources.getStringArray(R.array.equipment).toList()
                    Text(
                        text = traineesState.selectedTrainee?.second?.trainee?.availableEquipment?.joinToString(", ") { availableEquipmentList[it] } ?: "",
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth(0.95f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = traineesState.selectedTrainee?.second?.bio ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.95f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Requested appointment(s):",
                    fontSize = 20.sp,
                )
                val requestedAppointments =
                    traineesState.selectedTrainee?.first?.appointment
                requestedAppointments?.keys?.forEach {
                    Text(
                        text = buildString {
                            append("$it:  ")
                            requestedAppointments[it]?.forEachIndexed { index, hour ->
                                append("$hour:00")
                                if (index < requestedAppointments[it]!!.size -1) {
                                    append(", ")
                                }
                            }
                        }.trim(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row (
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = traineesState.appointmentsToCalendar,
                    onCheckedChange = {
                        traineesViewModel.onEvent(
                            TraineesEvent.AppointmentsToCalendar(it)
                        )
                    }
                )
                Text(
                    text = "Add appointment(s) to calendar",
                )
            }
            Row (
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                OutlinedButton(
                    onClick = {
                        traineesViewModel.onEvent(
                            TraineesEvent.DeclineRequest(
                                userId = traineesState.selectedTrainee?.first?.traineeId ?: ""
                            )
                        )
                        onBackClick()
                    },
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = Color.Red
                    )
                ) {
                    Text(
                        text = "Decline",
                        fontSize = 20.sp,
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedButton(
                    onClick = {
                        traineesViewModel.onEvent(
                            TraineesEvent.AcceptRequest(
                                userId = traineesState.selectedTrainee?.first?.traineeId ?: ""
                            )
                        )
                        onBackClick()
                    },
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = Color.Green
                    )
                ) {
                    Text(
                        text = "Accept",
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }
}
























