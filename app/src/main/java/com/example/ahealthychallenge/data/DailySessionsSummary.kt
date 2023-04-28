package com.example.ahealthychallenge.data

import java.time.DayOfWeek
import java.time.Duration
import java.time.Month

data class DailySessionsSummary(
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val month: Month = Month.APRIL,
    val dayOfMonth: Int = 0,
    val totalActiveTime: Duration? = null
)


