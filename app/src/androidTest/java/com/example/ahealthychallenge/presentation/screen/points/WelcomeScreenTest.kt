package com.example.ahealthychallenge.presentation.screen.points

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.WelcomeScreen
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.himanshoe.charty.pie.config.PieData
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val pieData = listOf(
        PieData(2F),
        PieData(2F),
        PieData(2F),
        PieData(2F)
    )

    @Test
    fun clickNavBarItemTest() {

        composeTestRule.setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val healthConnectManager = HealthConnectManager(LocalContext.current)
            val scaffoldState = rememberScaffoldState()
            val healthConnectAvailability by healthConnectManager.availability
            WelcomeScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                healthConnectAvailability = healthConnectAvailability,
                healthConnectManager = healthConnectManager,
                drawerNavController = navController,
                drawerScope = scope,
                scaffoldState = scaffoldState,
                onResumeAvailabilityCheck = {}
            )
        }

        composeTestRule.onNode(hasContentDescription("Home")).performClick().assertExists()
        composeTestRule.onNode(hasContentDescription("LeaderBoard")).performClick().assertExists()
        composeTestRule.onNode(hasContentDescription("Friends")).performClick().assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}
