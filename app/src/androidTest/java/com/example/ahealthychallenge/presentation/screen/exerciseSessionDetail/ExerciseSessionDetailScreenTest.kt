package com.example.ahealthychallenge.presentation.screen.exerciseSessionDetail

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.data.ExerciseSessionData
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailScreen
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModel
import org.junit.Rule
import org.junit.Test

class ExerciseSessionDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun totalDurationTest() {

        composeTestRule.setContent {
            ExerciseSessionDetailScreen(
                permissions = setOf(),
                permissionsGranted = true,
                sessionMetrics = ExerciseSessionData("ex"),
                uiState = ExerciseSessionDetailViewModel.UiState.Done
            )
        }
        composeTestRule.onNode(hasText("Total active duration")).assertExists()
        composeTestRule.onNode(hasText("00:00:00")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }


    @Test
    fun totalStepsTextTest() {

        composeTestRule.setContent {
            ExerciseSessionDetailScreen(
                permissions = setOf(),
                permissionsGranted = true,
                sessionMetrics = ExerciseSessionData("ex"),
                uiState = ExerciseSessionDetailViewModel.UiState.Done
            )
        }
        composeTestRule.onNode(hasText("Total steps")).assertExists()
        composeTestRule.onNode(hasText("0")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }


    @Test
    fun totalDistanceTextTest() {

        composeTestRule.setContent {
            ExerciseSessionDetailScreen(
                permissions = setOf(),
                permissionsGranted = true,
                sessionMetrics = ExerciseSessionData("ex"),
                uiState = ExerciseSessionDetailViewModel.UiState.Done
            )
        }
        composeTestRule.onNode(hasText("Total distance")).assertExists()
        composeTestRule.onNode(hasText("0.0")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun totalCaloriesTextTest() {

        composeTestRule.setContent {
            ExerciseSessionDetailScreen(
                permissions = setOf(),
                permissionsGranted = true,
                sessionMetrics = ExerciseSessionData("ex"),
                uiState = ExerciseSessionDetailViewModel.UiState.Done
            )
        }
        composeTestRule.onNode(hasText("Total calories")).assertExists()
        composeTestRule.onNode(hasText("null")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun speedStatsTextTest() {

        composeTestRule.setContent {
            ExerciseSessionDetailScreen(
                permissions = setOf(),
                permissionsGranted = true,
                sessionMetrics = ExerciseSessionData("ex"),
                uiState = ExerciseSessionDetailViewModel.UiState.Done
            )
        }
        composeTestRule.onNode(hasText("Speed stats (m/s)")).assertExists()
        composeTestRule.onNode(hasText("Min: n")).assertExists()
        composeTestRule.onNode(hasText("Max: n")).assertExists()
        composeTestRule.onNode(hasText("Avg: n")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun speedSeriesTextTest() {

        composeTestRule.setContent {
            ExerciseSessionDetailScreen(
                permissions = setOf(),
                permissionsGranted = true,
                sessionMetrics = ExerciseSessionData("ex"),
                uiState = ExerciseSessionDetailViewModel.UiState.Done
            )
        }
        composeTestRule.onNode(hasText("Speed series (m/s)")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun heartRateSeriesTextTest() {

        composeTestRule.setContent {
            ExerciseSessionDetailScreen(
                permissions = setOf(),
                permissionsGranted = true,
                sessionMetrics = ExerciseSessionData("ex"),
                uiState = ExerciseSessionDetailViewModel.UiState.Done
            )
        }
        composeTestRule.onNode(hasText("HR stats (bpm)")).assertExists()
        composeTestRule.onNode(hasText("Min: N/A")).assertExists()
        composeTestRule.onNode(hasText("Max: N/A")).assertExists()
        composeTestRule.onNode(hasText("Avg: N/A")).assertExists()
        composeTestRule.onNode(hasText("HR series (bpm)")).assertExists()

        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}