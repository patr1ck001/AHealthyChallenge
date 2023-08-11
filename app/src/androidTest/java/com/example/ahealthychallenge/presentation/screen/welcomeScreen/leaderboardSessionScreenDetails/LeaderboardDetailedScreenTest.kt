package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderboardSessionScreenDetails

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderBoadScreenDetails.LeaderBoardScreenDetails
import org.junit.Rule
import org.junit.Test

class LeaderboardDetailedScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun leaderboardDetailTest() {

        composeTestRule.setContent {
            LeaderBoardScreenDetails(
                userPointsSheet = UserPointsSheet(
                    pointsWalking = 2,
                    pointsCycling = 5,
                    totalPoints = 15,
                ),
                leaderboardDetailLoading = false
            )
        }

        //walking
        composeTestRule.onNode(hasText("Walking")).assertExists()
        composeTestRule.onNode(hasText("2")).assertExists()

        //running
        composeTestRule.onNode(hasText("Running")).assertDoesNotExist()

        //cycling
        composeTestRule.onNode(hasText("Biking")).assertExists()
        composeTestRule.onNode(hasText("5")).assertExists()

        //workout
        composeTestRule.onNode(hasText("Workout")).assertDoesNotExist()

        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}