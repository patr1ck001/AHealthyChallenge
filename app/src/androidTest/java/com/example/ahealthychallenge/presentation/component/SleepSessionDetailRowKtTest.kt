package com.example.ahealthychallenge.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test

class SleepSessionDetailRowKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun sleepSessionTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                Column {
                    SleepSessionDetailRow(
                        labelId = R.string.sleep_notes,
                        item = "Slept well"
                    )
                }
            }
        }

        composeTestRule.onNode(hasText("Notes:")).assertExists().assertIsDisplayed()
        composeTestRule.onNode(hasText("Slept well")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}