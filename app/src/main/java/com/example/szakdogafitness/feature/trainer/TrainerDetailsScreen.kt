package com.example.szakdogafitness.feature.trainer

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.R
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.RatingStars
import com.example.szakdogafitness.ui.common.RatingStarsTen
import com.example.szakdogafitness.ui.common.TrainerDemo
import com.example.szakdogafitness.ui.common.appbars.BackNavWithTitle
import com.example.szakdogafitness.util.calculateDistance
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.TrainersEvent
import com.example.szakdogafitness.viewmodel.TrainersViewModel

@Composable
fun TrainerDetailsScreen(
    modifier: Modifier = Modifier,
    data: Pair<Requests, User>,
    currentUser: User,
    trainersViewModel: TrainersViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    onNavBack: () -> Unit = {},
) {

    LaunchedEffect(Unit) {
        appBarViewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    BackNavWithTitle("${data.second.firstName} ${data.second.lastName}") {
                        onNavBack()
                    }
                }
            )
        )
        trainersViewModel.onEvent(
            TrainersEvent.LoadTrainersRate(
                trainerId = data.second.userId
            )
        )
    }

    val trainerState by trainersViewModel.state.collectAsStateWithLifecycle()

    val painter = rememberAsyncImagePainter(data.second.profileImageUrl)
    val painterState = painter.state.collectAsState()
    val context = LocalContext.current
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
                val specializationsList = context.resources.getStringArray(R.array.specializations).toList()
                /*Text(
                    text = data.trainer.specializations.joinToString(", ") {specializationsList[it]},
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )*/
                val specializationsText = remember { mutableStateOf(false) }
                Text(
                    text = if (specializationsText.value) {
                        data.second.trainer.specializations.joinToString(", ") { specializationsList[it] }
                    } else {
                        data.second.trainer.specializations.joinToString(", ") { specializationsList[it] }.take(50) + "..."
                    },
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .clickable { specializationsText.value = !specializationsText.value },
                    fontSize = 16.sp,
                    maxLines = if (specializationsText.value) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )
                val distance = data.second.location?.calculateDistance(currentUser.location)
                Text(
                    text = if (distance?.first != null) "${distance.first} km" else "N/A",
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                val genderList = context.resources.getStringArray(R.array.gender).toList()
                Text(
                    text = data.second.gender.let { genderList[it-1] },
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                val trainerRating = trainerState.allTrainersAllRating.find {
                    it?.first == data.second.userId
                }?.second?.sumOf {
                    it.rating
                }?.div(
                    trainerState.allTrainersAllRating.find { rating ->
                        rating?.first == data.second.userId
                    }?.second?.size ?: 1
                )
                val trainerRatingSize = trainerState.allTrainersAllRating.find {
                    it?.first == data.second.userId
                }?.second?.size ?: 1
                RatingStars(
                    rating = trainerRating ?: 0,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    rateSize = trainerRatingSize,
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
                text = "Contact Information",
                fontSize = 20.sp
            )
            Column (
                modifier = Modifier
                    .fillMaxWidth(0.95f),
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "message/rfc822"
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(data.second.email))
                            }
                            val chooser = Intent.createChooser(intent, "Send Email")
                            context.startActivity(chooser)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                        )
                    }
                    Text(
                        text = data.second.email,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                setData(Uri.parse("tel:${data.second.phoneNumber}"))
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                Log.d("TraineeBioScreen", "No dialer app found")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone",
                        )
                    }
                    Text(
                        text = data.second.phoneNumber,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = data.second.bio,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            /*LazyRow (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(data.second.trainer.socialMediaLinks.size) {
                    val socialMedia = data.second.trainer.socialMediaLinks.keys.toList()[it]
                    val link = data.second.trainer.socialMediaLinks.values.toList()[it]
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
                                .clickable{
                                    val intent = context.packageManager.getLaunchIntentForPackage(link)
                                    if (intent != null) {
                                        context.startActivity(intent)
                                    } else {
                                        Log.e("TrainerDetailsScreen", "Unable to open link: $link")
                                    }
                                }
                        )
                    }
                }
            }*/
            SocialMediaLazyRow(
                socialMediaLinks = data.second.trainer.socialMediaLinks
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "${data.second.trainer.pricing}$ / session",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                "Rate the trainer:",
            )
            RatingStarsTen(
                rating = trainerState.trainersRate.rating,
            ) { rating ->
                trainersViewModel.onEvent(
                    TrainersEvent.RateTrainer(
                        trainerId = data.second.userId,
                        rate = rating
                    )
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "Your appointment(s):",
                fontSize = 20.sp
            )
            data.first.appointment.keys.forEach {
                Text(
                    text = buildString {
                        append("$it: ")
                        data.first.appointment[it]?.forEach { appointment ->
                            append("$appointment:00")
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun SocialMediaLazyRow(socialMediaLinks: Map<String, String>) {
    val context = LocalContext.current

    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(socialMediaLinks.entries.toList()) { (platform, username) ->
            val painter = when (platform.lowercase()) {
                "facebook" -> painterResource(id = R.drawable.facebook)
                "instagram" -> painterResource(id = R.drawable.instagram)
                "linkedin" -> painterResource(id = R.drawable.linkedin)
                "pinterest" -> painterResource(id = R.drawable.pinterest)
                "tiktok" -> painterResource(id = R.drawable.tiktok)
                "x", "twitter" -> painterResource(id = R.drawable.twitter)
                "youtube" -> painterResource(id = R.drawable.youtube)
                else -> painterResource(id = R.drawable.tiktok)
            }

            val fullUrl = when (platform.lowercase()) {
                "instagram" -> "https://instagram.com/$username"
                "facebook" -> "https://facebook.com/$username"
                "linkedin" -> "https://linkedin.com/in/$username"
                "youtube" -> "https://www.youtube.com/@$username"
                "tiktok" -> "https://www.tiktok.com/@$username"
                "pinterest" -> "https://pinterest.com/$username"
                "x", "twitter" -> "https://x.com/$username"
                else -> "https://$platform.com/$username"
            }

            if (username.isNotEmpty()) {
                Image(
                    painter = painter,
                    contentDescription = platform,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .clickable {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Log.e("TrainerDetailsScreen", "Unable to open: $fullUrl", e)
                            }
                        }
                )
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
        data = Pair(Requests(), User()),
        currentUser = User(),
        trainersViewModel = hiltViewModel(),
    )
}
