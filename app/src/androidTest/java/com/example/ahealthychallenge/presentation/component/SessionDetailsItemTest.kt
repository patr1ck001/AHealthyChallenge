package com.example.ahealthychallenge.presentation.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.navigation.Screen
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test

class SessionDetailsItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun totalStepTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                LazyColumn {
                    sessionDetailsItem(R.string.total_steps) {
                        Text(text = "12345")
                    }
                }
            }
        }

        composeTestRule.onNode(hasText("Total steps")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun totalStepIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                LazyColumn {
                    sessionDetailsItem(R.string.total_steps) {
                        Text(text = "12345")
                    }
                }
            }
        }

        composeTestRule.onNode(hasText("12345")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

}