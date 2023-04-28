package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.Month

@Serializable
data class DailySessionSummarySerializable (
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val month: Month = Month.APRIL,
    val dayOfMonth: Int = 0,
    val totalActiveTime: DurationSerializable? = null
)