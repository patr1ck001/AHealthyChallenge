package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderboardScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.presentation.component.CircularProgressBar
import com.example.ahealthychallenge.presentation.theme.HealthConnectBlue
import com.example.ahealthychallenge.presentation.theme.HealthConnectBronze
import com.example.ahealthychallenge.presentation.theme.HealthConnectGold
import com.example.ahealthychallenge.presentation.theme.HealthConnectSilver

@Composable
fun LeaderBoardScreen(
    friends: List<Friend>,
    leaderboardLoading: Boolean,
    onDetailsClick: (String?) -> Unit = {},
) {
    if(!leaderboardLoading) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val position: MutableState<Int> = mutableIntStateOf(1)
            friends.forEachIndexed { index, friend ->
                Log.d("leaderboardDBUG", "the friends are $friends")
                item {
                    Card(
                        modifier = Modifier.clickable { onDetailsClick(friend.username) }

                    ) {
                        Row(
                            modifier = Modifier
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxHeight()
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    modifier = Modifier.padding(start = 10.dp, top = 30.dp),
                                    color = MaterialTheme.colors.primary,
                                    textAlign = TextAlign.Center
                                )
                            }

                            if (friend.bitmap != null) {
                                friend.bitmap?.let { bm ->
                                    Image(
                                        bitmap = bm.asImageBitmap(),
                                        contentDescription = "userImage",
                                        modifier = Modifier
                                            .size(80.dp) // TODO: ADAPTABLE LAYOUT
                                            .padding(8.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_profile_circle),
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(8.dp)
                                )
                            }
                            Column(
                                modifier = Modifier.weight(4f)
                            ) {
                                Row(horizontalArrangement = Arrangement.Center) {
                                    friend.username?.let { username ->
                                        Text(
                                            text = username,
                                            textAlign = TextAlign.Center,
                                            color = HealthConnectBlue,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    friend.firstName?.let {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    friend.lastName?.let {
                                        Text(
                                            text = it,
                                            modifier = Modifier
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "${friend.pointsSheet?.totalPoints} Points",
                                        color = MaterialTheme.colors.primary,
                                    )

                                }
                            }
                            when (index) {
                                0 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(top = 30.dp, end = 20.dp),
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_circle),
                                            contentDescription = null,
                                            tint = HealthConnectGold,
                                            modifier = Modifier
                                                .size(20.dp)
                                        )
                                    }
                                }

                                1 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(top = 30.dp, end = 20.dp),
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_circle),
                                            contentDescription = null,
                                            tint = HealthConnectSilver,
                                            modifier = Modifier
                                                .size(20.dp)
                                        )
                                    }
                                }

                                2 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(top = 30.dp, end = 20.dp),
                                        verticalArrangement = Arrangement.Center,

                                        ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_circle),
                                            contentDescription = null,
                                            tint = HealthConnectBronze,
                                            modifier = Modifier
                                                .size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                position.value += position.value
            }
        }
    }else {
        CircularProgressBar(
            isDisplayed = true, Modifier.size(60.dp)
        )
    }
}
