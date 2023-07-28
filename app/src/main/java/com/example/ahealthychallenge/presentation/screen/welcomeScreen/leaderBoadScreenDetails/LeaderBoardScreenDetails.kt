package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderBoadScreenDetails
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.ahealthychallenge.data.UserPointsSheet


@Composable
fun LeaderBoardScreenDetails (
    userPointsSheet: UserPointsSheet
) {
    Text(text = "walking: ${userPointsSheet.pointsWalking} \n running: ${userPointsSheet.pointRunning} \n total ${userPointsSheet.totalPoints} \n" )
}