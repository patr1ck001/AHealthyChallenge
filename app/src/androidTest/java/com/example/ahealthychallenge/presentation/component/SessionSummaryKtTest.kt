package com.example.ahealthychallenge.presentation.component

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.R
import org.junit.Rule
import org.junit.Test

class SessionSummaryKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun exerciseSessionTest() {

        composeTestRule.setContent {
            SessionSummary(labelId = R.string.points, value = "15")
        }
        composeTestRule.onNode(hasText("15 points")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}
