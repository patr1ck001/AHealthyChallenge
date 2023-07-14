package com.example.ahealthychallenge.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import com.example.ahealthychallenge.presentation.component.CircularProgressBar
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen.HomeScreen
import com.example.ahealthychallenge.presentation.utils.NavigationType
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