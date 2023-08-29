package com.example.ahealthychallenge.presentation.screen.profile

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.presentation.utils.NavigationType
import org.junit.Rule
import org.junit.Test

class ProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileTest() {

        composeTestRule.setContent {
            ProfileScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                currentUser = Friend(
                    "Patrick", "Niantcho", null, "pat", null,
                    UserPointsSheet(
                        pointsWalking = 2,
                        pointRunning = 3,
                        pointsCycling = 5,
                        totalPoints = 10,
                    )
                ),
                profileLoading = true,
                positionInLeaderboard = 1,
                pointTHisMonth = 10,
                totalFriends = 5
            )
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}