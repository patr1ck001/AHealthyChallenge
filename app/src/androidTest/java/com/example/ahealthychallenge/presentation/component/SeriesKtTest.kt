package com.example.ahealthychallenge.presentation.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.health.connect.client.records.HeartRateRecord
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.ZoneId

class SeriesKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                LazyColumn {
                    val time1 = Instant.now()
                    val time2 = time1.minusSeconds(60)
                    heartRateSeries(
                        labelId = R.string.hr_series,
                        series = listOf(
                            HeartRateRecord(
                                startTime = time2,
                                startZoneOffset = ZoneId.systemDefault().rules.getOffset(time2),
                                endTime = time1,
                                endZoneOffset = ZoneId.systemDefault().rules.getOffset(time1),
                                samples = listOf(
                                    HeartRateRecord.Sample(
                                        beatsPerMinute = 103,
                                        time = time1
                                    ),
                                    HeartRateRecord.Sample(
                                        beatsPerMinute = 85,
                                        time = time2
                                    )
                                )
                            )
                        )
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("HR series (bpm)")).assertExists().assertIsDisplayed()
        composeTestRule.onNode(hasText("Sample: 85")).assertExists().assertIsDisplayed()
        composeTestRule.onNode(hasText("Sample: 103")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
}