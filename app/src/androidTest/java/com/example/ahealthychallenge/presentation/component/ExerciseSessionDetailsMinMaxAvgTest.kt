package com.example.ahealthychallenge.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test

class ExerciseSessionDetailsMinMaxAvgTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun totalStepTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                ExerciseSessionDetailsMinMaxAvg(minimum = "10", maximum = "100", average = "55")
            }
        }

        composeTestRule.onNode(hasText("Avg: 55")).assertExists().assertIsDisplayed()
        composeTestRule.onNode(hasText("Max: 100")).assertExists().assertIsDisplayed()
        composeTestRule.onNode(hasText("Min: 10")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}