package com.example.szakdogafitness.ui.common

import android.R.attr.fontWeight
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.R


data class TrainerDemo (
    val Fname: String,
    val Lname: String,
    val image: String,
    val rating: Int,
    val price: Int,
    val distance : Float,
    val gender : String,
    val bio : String = "",
    val specializations : List<String>,
    val socialMediaLinks : Map<String, String> = mapOf(),
    val appointments : Map<String, List<Int>> = mapOf()
)

@Composable
fun TrainerCard(
    data : TrainerDemo
) {
    val painter = rememberAsyncImagePainter(data.image)
    val painterState = painter.state.collectAsState()
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {}
    ) {
        ConstraintLayout (
            modifier = Modifier
                .fillMaxWidth()
        ){
            val (image, name, rating, price, distance, gender, specializations) = createRefs()

            when (painterState.value) {
                AsyncImagePainter.State.Empty -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp)
                            .constrainAs(image) {
                                top.linkTo(parent.top, margin = 8.dp)
                                start.linkTo(parent.start, margin = 8.dp)
                            }
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Trainer Image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(80.dp)
                            .constrainAs(image) {
                                top.linkTo(parent.top, margin = 8.dp)
                                start.linkTo(parent.start, margin = 8.dp)
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(80.dp)
                            .constrainAs(image) {
                                top.linkTo(parent.top, margin = 8.dp)
                                start.linkTo(parent.start, margin = 8.dp)
                            }
                    )
                }
                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = "Trainer Image",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(80.dp)
                            .constrainAs(image) {
                                top.linkTo(parent.top, margin = 8.dp)
                                start.linkTo(parent.start, margin = 8.dp)
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(
                text = "${data.Fname} ${data.Lname}",
                modifier = Modifier
                    .constrainAs(name) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(image.end, margin = 16.dp)
                    },
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = Bold
            )
            data.specializations.forEach {
                Text(
                    text = data.specializations.joinToString(", "),
                    modifier = Modifier
                        .constrainAs(specializations){
                            top.linkTo(name.bottom, margin = 8.dp)
                            start.linkTo(image.end, margin = 16.dp)
                            end.linkTo(parent.end, margin = 8.dp)
                            width = Dimension.fillToConstraints
                        },
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                    )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(rating){
                        top.linkTo(image.bottom, margin = 15.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                },
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingStars(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    rating = data.rating
                )
                Text(
                    text = "$${data.price} / session",
                    modifier = Modifier.weight(1f),
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = Bold,
                    textAlign = TextAlign.Center
                )
                Column (
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${data.distance} miles away",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = Bold
                    )
                    Text(
                        text = data.gender,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = Bold
                    )
                }
            }

        }
    }
}

@Composable
fun RatingStars(
    modifier: Modifier = Modifier,
    rating: Int
){
    Row (
        modifier = modifier
            //.padding(8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(rating / 2) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        if (rating % 2 == 1) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.StarHalf,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        repeat(5 - (rating + 1) / 2) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
@Preview
fun TrainerCardPreview() {
    val trainer = TrainerDemo(
        Fname = "John",
        Lname = "Doe",
        image = "https://www.google.com",
        rating = 7,
        price = 5000,
        distance = 5.0f,
        gender = "male",
        specializations = listOf("cardio", "yoga", "pilates", "weightlifting", "crossfit")
    )
    TrainerCard(
        data = trainer
    )
}