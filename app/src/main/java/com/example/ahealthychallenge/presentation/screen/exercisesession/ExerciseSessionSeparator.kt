package com.example.ahealthychallenge.presentation.screen.exercisesession

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.DailySessionsSummary
import com.example.ahealthychallenge.data.formatTime
import java.time.DayOfWeek
import java.time.Duration
import java.time.Month
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Composable
fun ExerciseSessionSeparator(
    dailySessionsSummary: DailySessionsSummary,
    points: Int,
    modifier: Modifier
) {
    val today = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
    val yesterday = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(1)
    Column(
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (
                dailySessionsSummary.date.year == today.year &&
                dailySessionsSummary.date.month == today.month &&
                dailySessionsSummary.date.dayOfMonth == today.dayOfMonth
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.caption,
                    text = stringResource(R.string.today)
                )
            } else if (
                dailySessionsSummary.date.year == yesterday.year &&
                dailySessionsSummary.date.month == yesterday.month &&
                dailySessionsSummary.date.dayOfMonth == yesterday.dayOfMonth
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.caption,
                    text = stringResource(R.string.yesterday)
                )
            } else {
                Text(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.caption,
                    text = stringResource(
                        R.string.session_summary_layout,
                        getDayOfWeek(dailySessionsSummary.date.dayOfWeek),
                        getMonth(dailySessionsSummary.date.month),
                        dailySessionsSummary.date.dayOfMonth.toString()
                    )
                )
            }

            Text(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.caption,

                text = stringResource(
                    R.string.summary_layout,
                    points.toString() ?: "N/A",
                    stringResource(R.string.points),
                )
            )

            Text(
                modifier = Modifier.weight(
                    1f,
                    false
                ), // to align the element to the bottom of the row
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.caption,
                text = dailySessionsSummary.totalActiveTime?.formatTime().toString()
            )
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = modifier
        )
    }
}

fun getMonth(month: Month): String {
    return when (month) {
        Month.JANUARY -> "Jan"
        Month.FEBRUARY -> "Feb"
        Month.MARCH -> "Mar"
        Month.APRIL -> "Apr"
        Month.MAY -> "May"
        Month.JUNE -> "Jun"
        Month.JULY -> "Jul"
        Month.AUGUST -> "Aug"
        Month.SEPTEMBER -> "Sep"
        Month.OCTOBER -> "Oct"
        Month.NOVEMBER -> "Nov"
        Month.DECEMBER -> "Dec"
    }
}

fun getDayOfWeek(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }
}

@Preview
@Composable
fun ExerciseSessionSeparatorPreview() {
    ExerciseSessionSeparator(
        dailySessionsSummary = DailySessionsSummary(
            ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS),
            Duration.ofSeconds(2000)
        ),
        points = 2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp),
    )
}