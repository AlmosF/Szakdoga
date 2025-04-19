package com.example.szakdogafitness.ui.common.trainee

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.R
import com.example.szakdogafitness.domain.model.User
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TraineeCard(
    modifier: Modifier = Modifier,
    data: User,
    onCardClick : () -> Unit,
    onMessageClick : () -> Unit
) {
    val painter = rememberAsyncImagePainter(data.profileImageUrl)
    val painterState = painter.state.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            onCardClick()
        }
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier
                    .weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (painterState.value) {
                    AsyncImagePainter.State.Empty -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(80.dp)
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Trainee Image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(80.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(80.dp)

                        )
                    }
                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = painter,
                            contentDescription = "Trainee Image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(80.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Column (
                modifier = Modifier
                    .weight(4f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${data.firstName} ${data.lastName}",
                )
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data.dateOfBirth?.toDate()).toString()
                )
                Text(
                    text = data.phoneNumber
                )
            }
            /*Column (
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        onMessageClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Message,
                        contentDescription = "Message",
                    )
                }
            }*/
        }
    }
}

@Composable
@Preview
fun TraineeCardPreview(

) {
    TraineeCard(
        data = User(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "123456789",
            profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"
        ),
        onCardClick = {},
        onMessageClick = {}
    )
}