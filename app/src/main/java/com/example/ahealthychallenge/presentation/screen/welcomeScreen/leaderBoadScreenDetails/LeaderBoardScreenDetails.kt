package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderBoadScreenDetails

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.presentation.SignInActivity
import com.example.ahealthychallenge.presentation.component.CircularProgressBar
import com.example.ahealthychallenge.presentation.component.sessionDetailsItem
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils

@Composable
fun LeaderBoardScreenDetails(
    userPointsSheet: UserPointsSheet,
    leaderboardDetailLoading: Boolean
) {
    if (!leaderboardDetailLoading) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 16.dp),
        ) {

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_step),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Walking",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${userPointsSheet.pointsWalking}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(30.dp))

                // number of friends row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_runner),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Running",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${userPointsSheet.pointRunning}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_biking),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Biking",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${userPointsSheet.pointsCycling}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                // Position row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        elevation = 10.dp,
                        shape = CircleShape,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_workout),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Workout",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${userPointsSheet.pointsWorkout}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    } else {
        CircularProgressBar(
            isDisplayed = true, Modifier.size(60.dp)
        )
    }
}