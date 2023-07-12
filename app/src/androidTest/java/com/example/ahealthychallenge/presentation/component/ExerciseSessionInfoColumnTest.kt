package com.example.ahealthychallenge.presentation.component

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.health.connect.client.units.Length
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.ZonedDateTime

class ExerciseSessionInfoColumnTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textExerciseIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                ExerciseSessionInfoColumn(
                    modifier = Modifier,
                    exerciseType = 5,
                    start = ZonedDateTime.now(),
                    end = ZonedDateTime.now().plusHours(2),
                    duration = Duration.ofHours(2),
                    points = 2,
                    uid = "uid",
                    name = "biking",
                    steps = "0",
                    sourceAppName = "Basketball",
                    sourceAppIcon = AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_samsung_health_logo),
                    distance = Length.kilometers(10.0)
                )
            }
        }
        composeTestRule.onNode(hasText("Basketball")).assertExists().assertIsDisplayed()
    }

    @Test
    fun textAppIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                ExerciseSessionInfoColumn(
                    modifier = Modifier,
                    exerciseType = 5,
                    start = ZonedDateTime.now(),
                    end = ZonedDateTime.now().plusHours(2),
                    duration = Duration.ofHours(2),
                    points = 2,
                    uid = "uid",
                    name = "biking",
                    steps = "0",
                    sourceAppName = "Unknown app",
                    sourceAppIcon = AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_samsung_health_logo),
                    distance = Length.kilometers(10.0)
                )
            }
        }
        composeTestRule.onNode(hasText("Basketball")).assertExists().assertIsDisplayed()
    }

    @Test
    fun textDistanceIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                ExerciseSessionInfoColumn(
                    modifier = Modifier,
                    exerciseType = 5,
                    start = ZonedDateTime.now(),
                    end = ZonedDateTime.now().plusHours(2),
                    duration = Duration.ofHours(2),
                    points = 2,
                    uid = "uid",
                    name = "biking",
                    steps = "0",
                    sourceAppName = "Unknown app",
                    sourceAppIcon = AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_samsung_health_logo),
                    distance = Length.kilometers(10.0)
                )
            }
        }
        composeTestRule.onNode(hasText("10.00 Km")).assertExists().assertIsDisplayed()
    }

    @Test
    fun textPointsIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                ExerciseSessionInfoColumn(
                    modifier = Modifier,
                    exerciseType = 5,
                    start = ZonedDateTime.now(),
                    end = ZonedDateTime.now().plusHours(2),
                    duration = Duration.ofHours(2),
                    points = 2,
                    uid = "uid",
                    name = "biking",
                    steps = "0",
                    sourceAppName = "Unknown app",
                    sourceAppIcon = AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_samsung_health_logo),
                    distance = Length.kilometers(10.0)
                )
            }
        }
        composeTestRule.onNode(hasText("2 points")).assertExists().assertIsDisplayed()
    }

    @Test
    fun appIconIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                ExerciseSessionInfoColumn(
                    modifier = Modifier,
                    exerciseType = 5,
                    start = ZonedDateTime.now(),
                    end = ZonedDateTime.now().plusHours(2),
                    duration = Duration.ofHours(2),
                    points = 2,
                    uid = "uid",
                    name = "biking",
                    steps = "0",
                    sourceAppName = "Unknown app",
                    sourceAppIcon = AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_samsung_health_logo),
                    distance = Length.kilometers(10.0)
                )
            }
        }
        composeTestRule.onNode(hasContentDescription("App Icon")).assertExists().assertIsDisplayed()
    }
}