package com.example.ahealthychallenge.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class CircularProgressBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun circularBarTest() {
        composeTestRule.setContent {
            CircularProgressBar(isDisplayed = true, modifier = Modifier.fillMaxSize(1f))
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}