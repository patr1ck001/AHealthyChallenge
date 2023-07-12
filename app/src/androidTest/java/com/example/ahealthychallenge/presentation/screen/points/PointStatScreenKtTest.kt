package com.example.ahealthychallenge.presentation.screen.points

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.printToLog
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.himanshoe.charty.line.model.LineData
import com.himanshoe.charty.pie.config.PieData
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PointStatScreenKtTest {

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
    fun pointExerciseTextTest() {

        composeTestRule.setContent {
            PointStatScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                pieData = pieData,
                curveLineData = lineChartData,
                walkingLineData = lineChartData,
                runningLineData = lineChartData,
                bikingLineData = lineChartData,
                workoutLineData = lineChartData
            )
        }
        composeTestRule.onNode(hasText("Points per exercise type")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun pointMonthTextTest() {

        composeTestRule.setContent {
            PointStatScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                pieData = pieData,
                curveLineData = lineChartData,
                walkingLineData = lineChartData,
                runningLineData = lineChartData,
                bikingLineData = lineChartData,
                workoutLineData = lineChartData
            )
        }
        composeTestRule.onNode(hasScrollToIndexAction()).performScrollToKey(4)
        composeTestRule.onNode(hasText("Points this month")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun pointWalkingTextTest() {

        composeTestRule.setContent {
            PointStatScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                pieData = pieData,
                curveLineData = lineChartData,
                walkingLineData = lineChartData,
                runningLineData = lineChartData,
                bikingLineData = lineChartData,
                workoutLineData = lineChartData
            )
        }
        composeTestRule.onNode(hasScrollToIndexAction()).performScrollToKey(6)
        composeTestRule.onNode(hasText("Points walking")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }


    @Test
    fun pointRunningTextTest() {

        composeTestRule.setContent {
            PointStatScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                pieData = pieData,
                curveLineData = lineChartData,
                walkingLineData = lineChartData,
                runningLineData = lineChartData,
                bikingLineData = lineChartData,
                workoutLineData = lineChartData
            )
        }
        composeTestRule.onNode(hasScrollToIndexAction()).performScrollToKey(8)
        composeTestRule.onNode(hasText("Points running")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun pointBikingTextTest() {

        composeTestRule.setContent {
            PointStatScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                pieData = pieData,
                curveLineData = lineChartData,
                walkingLineData = lineChartData,
                runningLineData = lineChartData,
                bikingLineData = lineChartData,
                workoutLineData = lineChartData
            )
        }
        composeTestRule.onNode(hasScrollToIndexAction()).performScrollToKey(10)
        composeTestRule.onNode(hasText("Points biking")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun pointWorkoutTextTest() {
        composeTestRule.setContent {
            PointStatScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                pieData = pieData,
                curveLineData = lineChartData,
                walkingLineData = lineChartData,
                runningLineData = lineChartData,
                bikingLineData = lineChartData,
                workoutLineData = lineChartData
            )
        }
        composeTestRule.onNode(hasScrollToIndexAction()).performScrollToKey(12)
        composeTestRule.onNode(hasText("Points working out")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}