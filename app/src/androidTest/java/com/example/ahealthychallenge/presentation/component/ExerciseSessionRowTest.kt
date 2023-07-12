package com.example.ahealthychallenge.presentation.component

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.health.connect.client.units.Length
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import com.example.ahealthychallenge.R
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.ZonedDateTime
import java.util.UUID

class ExerciseSessionRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun textExerciseIsDisplayed() {
        composeTestRule.setContent {
            val context = LocalContext.current
            HealthConnectTheme {
                ExerciseSessionRow(
                    1,
                    ZonedDateTime.now().minusMinutes(30),
                    ZonedDateTime.now(),
                    Duration.ZERO,
                    points = 2,
                    UUID.randomUUID().toString(),
                    "Running",
                    "0",
                    sourceAppName = "My Fitness app",
                    sourceAppIcon = context.getDrawable(R.drawable.ic_launcher_foreground),
                    distance = Length.meters(100.0)
                )
            }
        }
        composeTestRule.onNode(hasText("Workout")).assertExists().assertIsDisplayed()
    }

    @Test
    fun textAppIsDisplayed() {
        composeTestRule.setContent {
            val context = LocalContext.current
            HealthConnectTheme {
                ExerciseSessionRow(
                    1,
                    ZonedDateTime.now().minusMinutes(30),
                    ZonedDateTime.now(),
                    Duration.ZERO,
                    points = 2,
                    UUID.randomUUID().toString(),
                    "Running",
                    "0",
                    sourceAppName = "My Fitness app",
                    sourceAppIcon = context.getDrawable(R.drawable.ic_launcher_foreground),
                    distance = Length.meters(100.0)
                )
            }
        }
        composeTestRule.onNode(hasText("My Fitness app")).assertExists().assertIsDisplayed()
    }

    @Test
    fun textPointsIsDisplayed() {
        composeTestRule.setContent {
            val context = LocalContext.current
            HealthConnectTheme {
                ExerciseSessionRow(
                    1,
                    ZonedDateTime.now().minusMinutes(30),
                    ZonedDateTime.now(),
                    Duration.ZERO,
                    points = 2,
                    UUID.randomUUID().toString(),
                    "Running",
                    "0",
                    sourceAppName = "My Fitness app",
                    sourceAppIcon = context.getDrawable(R.drawable.ic_launcher_foreground),
                    distance = Length.meters(100.0)
                )
            }
        }
        composeTestRule.onNode(hasText("2 points")).assertExists().assertIsDisplayed()
    }

    @Test
    fun textDistanceIsDisplayed() {
        composeTestRule.setContent {
            val context = LocalContext.current
            HealthConnectTheme {
                ExerciseSessionRow(
                    1,
                    ZonedDateTime.now().minusMinutes(30),
                    ZonedDateTime.now(),
                    Duration.ZERO,
                    points = 2,
                    UUID.randomUUID().toString(),
                    "Running",
                    "0",
                    sourceAppName = "My Fitness app",
                    sourceAppIcon = context.getDrawable(R.drawable.ic_launcher_foreground),
                    distance = Length.meters(100.0)
                )
            }
        }
        composeTestRule.onNode(hasText("0.10 Km")).assertExists().assertIsDisplayed()
    }

    @Test
    fun appIconIsDisplayed() {
        composeTestRule.setContent {
            val context = LocalContext.current
            HealthConnectTheme {
                ExerciseSessionRow(
                    1,
                    ZonedDateTime.now().minusMinutes(30),
                    ZonedDateTime.now(),
                    Duration.ZERO,
                    points = 2,
                    UUID.randomUUID().toString(),
                    "Running",
                    "0",
                    sourceAppName = "My Fitness app",
                    sourceAppIcon = context.getDrawable(R.drawable.ic_launcher_foreground),
                    distance = Length.meters(100.0)
                )
            }
        }
        composeTestRule.onNode(hasContentDescription("App Icon")).assertExists().assertIsDisplayed()
    }
}