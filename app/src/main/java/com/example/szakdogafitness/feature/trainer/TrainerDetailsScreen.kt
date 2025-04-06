package com.example.szakdogafitness.feature.trainer

import android.R.attr.bottom
import android.R.attr.text
import android.R.attr.top
import android.R.attr.visible
import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.ui.common.RatingStars
import com.example.szakdogafitness.ui.common.TrainerDemo
import com.example.szakdogafitness.R
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.RatingStarsTen
import com.example.szakdogafitness.ui.common.TraineeTimetableView
import com.example.szakdogafitness.util.calculateDistance
import com.example.szakdogafitness.viewmodel.TrainersEvent
import com.example.szakdogafitness.viewmodel.TrainersViewModel
import com.google.firebase.firestore.GeoPoint

@Composable
fun TrainerDetailsScreen(
    modifier: Modifier = Modifier,
    //data: TrainerDemo
    data: User,
    currentUser: User,
    trainersViewModel: TrainersViewModel = hiltViewModel(),
    //VMs
) {

    LaunchedEffect(Unit) {
        trainersViewModel.onEvent(TrainersEvent.FetchTrainersRequests(data.userId))
        //trainersViewModel.onEvent(TrainersEvent.FetchRequestedAppointments(data.userId))
        trainersViewModel.onEvent(TrainersEvent.FetchRequestedAppoint(data.userId))
    }

    val trainerState by trainersViewModel.state.collectAsStateWithLifecycle()

    val painter = rememberAsyncImagePainter(data.profileImageUrl)
    val painterState = painter.state.collectAsState()


    var scheduleOpened by remember { mutableStateOf(false) }
    Column (
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
            ) {
                when (painterState.value) {
                    AsyncImagePainter.State.Empty -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_report_image),
                            contentDescription = "Error",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(shape = RoundedCornerShape(16.dp))
                        )
                    }
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
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
                }
            }
            Column (
                modifier = Modifier
                    .weight(2f)
                    .heightIn(150.dp)
                    .fillMaxWidth()
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = data.trainer.specializations.joinToString(", "),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                val distance = data.location?.calculateDistance(currentUser.location)
                Text(
                    text = if (distance?.first != null) "${distance.first} km" else "N/A",
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                Text(
                    text = data.gender.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                RatingStars(
                    rating = data.trainer.rating,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    rateSize = 0
                )
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth(0.95f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.bio,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //items(data.socialMediaLinks.size) {
                items(data.trainer.socialMediaLinks.size) {
                    val socialMedia = data.trainer.socialMediaLinks.keys.toList()[it]
                    val link = data.trainer.socialMediaLinks.values.toList()[it]
                    /*val painter = when (socialMedia.lowercase()) {
                        "facebook" -> painterResource(id = R.drawable.facebook)
                        "instagram" -> painterResource(id = R.drawable.instagram)
                        "x" -> painterResource(id = R.drawable.twitter)
                        else -> painterResource(id = R.drawable.tiktok)
                    }*/
                    val painter = when {
                        socialMedia.equals("facebook", ignoreCase = true) -> painterResource(id = R.drawable.facebook)
                        socialMedia.equals("instagram", ignoreCase = true) -> painterResource(id = R.drawable.instagram)
                        socialMedia.equals("linkedin", ignoreCase = true) -> painterResource(id = R.drawable.linkedin)
                        socialMedia.equals("pinterest", ignoreCase = true) -> painterResource(id = R.drawable.pinterest)
                        socialMedia.equals("tiktok", ignoreCase = true) -> painterResource(id = R.drawable.tiktok)
                        socialMedia.equals("x", ignoreCase = true) -> painterResource(id = R.drawable.twitter)
                        socialMedia.equals("youtube", ignoreCase = true) -> painterResource(id = R.drawable.youtube)
                        else -> painterResource(id = R.drawable.tiktok)
                    }
                    if (link.isNotEmpty()){
                        Image(
                            painter = painter,
                            contentDescription = socialMedia,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            AnimatedVisibility(
                visible = trainerState.requestedAppoint.first.appointment.isEmpty()
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select training schedules",
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { scheduleOpened = !scheduleOpened }
                    ) {
                        Icon(
                            imageVector = if (scheduleOpened)  Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle schedule"
                        )
                    }
                }
            }
            var selectedSchedules by remember { mutableStateOf(mapOf<String, List<Int>>()) }
            AnimatedVisibility(
                visible = scheduleOpened && trainerState.requestedAppoint.first.appointment.isEmpty(),
            ) {
                Log.e("TrainerDetailsScreen", "AVAILABLE: ${trainerState.trainersRequests}")
                Log.e("TrainerDetailsScreen", "SELECTED: ${trainerState.requestedAppointments}")
                Log.e("TrainerDetailsScreen", "SELECTED: ${trainerState.requestedAppoint.first.appointment}")
                TraineeTimetableView(
                    availableHours = data.trainer.availability.mapValues { (day, hours) ->
                        hours - (trainerState.trainersRequests[day] ?: emptyList())
                    },
                    selectedHours = selectedSchedules
                ) { key, value ->
                    val updatedSchedules = selectedSchedules.toMutableMap().apply {
                        val hours = getOrDefault(key, emptyList()).toMutableList()
                        if (hours.contains(value)) {
                            hours.remove(value)
                            if (hours.isEmpty()) {
                                remove(key)
                            } else {
                                put(key, hours)
                            }
                        } else {
                            hours.add(value)
                            put(key, hours)
                        }
                    }
                    selectedSchedules = updatedSchedules
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "${data.trainer.pricing}$ / session",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.size(16.dp))
            if (trainerState.requestedAppoint.first.status != "accepted") {
                OutlinedButton(
                    onClick = {
                        trainersViewModel.onEvent(
                            TrainersEvent.ApplyForAppointment(
                                trainer = data.userId,
                                appointment = selectedSchedules, //fixme: NOT THIS VAR
                            )
                        )
                    },
                    //enabled = trainerState.requestedAppointments.isEmpty(),
                    enabled = trainerState.requestedAppoint.first.appointment.isEmpty() &&
                    selectedSchedules.isNotEmpty(),
                ) {
                    /*Text(
                        //text = if (trainerState.requestedAppointments.isEmpty()) "Request as Trainer" else "You already requested an appointment",
                        text = if (trainerState.requestedAppoint.first.appointment.isEmpty())
                            "Request as Trainer"
                        else if(trainerState.requestedAppoint.first.status == "pending")
                            "You already requested an appointment"
                        else
                            "You already have an appointment",
                    )*/
                    Text(
                        //text = if (trainerState.requestedAppointments.isEmpty()) "Request as Trainer" else "You already requested an appointment",
                        text = if (trainerState.requestedAppoint.first.appointment.isEmpty())
                            "Request as Trainer"
                        else
                            "You already requested an appointment",
                    )
                }
            } else {
                LaunchedEffect(Unit) {
                    trainersViewModel.onEvent(
                        TrainersEvent.LoadTrainersRate(
                            trainerId = data.userId
                        )
                    )
                }
                Text(
                    "Rate the trainer:",
                )
                RatingStarsTen(
                    rating = trainerState.trainersRate.rating,
                ) { rating ->
                    trainersViewModel.onEvent(
                        TrainersEvent.RateTrainer(
                            trainerId = data.userId,
                            rate = rating
                        )
                    )
                }

            }
        }

    }
}



@Composable
@Preview
fun TrainerDetailsScreenPreview() {
    val data = TrainerDemo(
        Fname = "Johnny",
        Lname = "Depp",
        image = "https://wepublishnews.com/wp-content/uploads/2021/05/Johnny-Depp-Net-Worth.jpg",
        rating = 10,
        price = 50,
        distance = 5.0f,
        gender = "male",
        specializations = listOf("cardio", "yoga", "pilates", "weightlifting", "crossfit", "bodybuilding"),
        bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        socialMediaLinks = mapOf(
            "facebook" to "https://www.facebook.com",
            "instagram" to "https://www.instagram.com",
            "X" to "https://www.x.com"
        ),
        appointments = mapOf(
            "Monday" to List(4) { it + 1 },
            "Tuesday" to List(4) { it + 1 },
            "Wednesday" to List(4) { it + 1 },
        )
    )
    TrainerDetailsScreen(
        data = User(),
        currentUser = User(),
        trainersViewModel = hiltViewModel(),
    )
}
