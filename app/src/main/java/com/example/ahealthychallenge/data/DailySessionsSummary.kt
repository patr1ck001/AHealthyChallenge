package com.example.ahealthychallenge.data

import java.time.DayOfWeek
import java.time.Duration
import java.time.Month
import java.time.ZonedDateTime

data class DailySessionsSummary(
    val date: ZonedDateTime = ZonedDateTime.now(),
    val totalActiveTime: Duration? = null,
    val totalPoints: Int = 0
)


