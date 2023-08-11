package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderboardScreen

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.presentation.screen.profile.ProfileScreen
import com.example.ahealthychallenge.presentation.utils.NavigationType
import org.junit.Rule
import org.junit.Test

class LeaderBoardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun leaderboardTest() {

        composeTestRule.setContent {
            LeaderBoardScreen(friends = listOf(
                Friend(
                    "Patrick", "Niantcho", null, "pat", null,
                    UserPointsSheet(
                        pointsWalking = 2,
                        pointRunning = 3,
                        pointsCycling = 5,
                        totalPoints = 10,
                    )
                ),
                Friend(
                    "Orelien", "Njanda", null, "oreo", null,
                    UserPointsSheet(
                        pointsWalking = 2,
                        pointRunning = 4,
                        pointsCycling = 5,
                        totalPoints = 11,
                    )
                )
            ), leaderboardLoading = false)
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}