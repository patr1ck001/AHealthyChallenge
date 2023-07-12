package com.example.ahealthychallenge.presentation.component

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime

class FormattedChangesTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                FormattedChangeRow(
                    startTime = ZonedDateTime.now().withNano(0),
                    recordType = stringResource(id = R.string.differential_changes_type_exercise_session),
                    dataSource = LocalContext.current.packageName
                )
            }
        }
        composeTestRule.onNode(hasText("Upserted:")).assertExists().assertIsDisplayed()
    }



    @Test
    fun textExerciseIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                FormattedChangeRow(
                    startTime = ZonedDateTime.now().withNano(0),
                    recordType = stringResource(id = R.string.differential_changes_type_exercise_session),
                    dataSource = LocalContext.current.packageName
                )
            }
        }
        composeTestRule.onNode(hasText("Exercise session")).assertExists().assertIsDisplayed()
    }

    @Test
    fun textPackageNameIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                FormattedChangeRow(
                    startTime = ZonedDateTime.now().withNano(0),
                    recordType = stringResource(id = R.string.differential_changes_type_exercise_session),
                    dataSource = LocalContext.current.packageName
                )
            }
        }
        composeTestRule.onNode(hasText("com.example.ahealthychallenge")).assertExists().assertIsDisplayed()
    }

}
