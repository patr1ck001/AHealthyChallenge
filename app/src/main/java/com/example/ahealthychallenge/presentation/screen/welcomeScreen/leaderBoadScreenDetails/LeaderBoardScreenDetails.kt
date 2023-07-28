package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderBoadScreenDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.presentation.component.sessionDetailsItem

@Composable
fun LeaderBoardScreenDetails(
    userPointsSheet: UserPointsSheet
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        sessionDetailsItem(labelId = R.string.points_walking) {
            Text("${userPointsSheet.pointsWalking}")
        }

        sessionDetailsItem(labelId = R.string.points_running) {
            Text("${userPointsSheet.pointRunning}")
        }

        sessionDetailsItem(labelId = R.string.points_biking) {
            Text("${userPointsSheet.pointsCycling}")
        }

        sessionDetailsItem(labelId = R.string.points_working_out) {
            Text("${userPointsSheet.pointsWorkout}")
        }

        sessionDetailsItem(labelId = R.string.total_points) {
            Text("${userPointsSheet.totalPoints}")
        }
    }
}