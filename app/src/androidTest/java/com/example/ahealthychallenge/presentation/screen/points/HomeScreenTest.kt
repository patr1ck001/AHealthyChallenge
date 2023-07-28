package com.example.ahealthychallenge.presentation.screen.points

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen.HomeScreen
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.himanshoe.charty.line.model.LineData
import com.himanshoe.charty.pie.config.PieData
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private val pieData = listOf(
        PieData(2F),
        PieData(2F),
        PieData(2F),
        PieData(2F)
    )

    private val lineChartData = listOf(
        LineData(10F, 35F),
        LineData(20F, 25F),
        LineData(10F, 50F),
        LineData(80F, 10F),
        LineData(10F, 15F),
        LineData(50F, 100F),
        LineData(20F, 25F),
    )

    @Test
    fun pointMonthTextTest() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState()
            HomeScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                lineData = lineChartData,
                drawerNavController = navController,
                drawerScope = scope,
                scaffoldState = scaffoldState
            )
        }
        composeTestRule.onNode(hasText("Points this month")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun navigationPointScreenOnClickTest() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState()
            HomeScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                lineData = lineChartData,
                drawerNavController = navController,
                drawerScope = scope,
                scaffoldState = scaffoldState
            )
        }

        val clickableCard = composeTestRule.onNode(hasTestTag("clickableCard"), useUnmergedTree = true)
        clickableCard.assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}