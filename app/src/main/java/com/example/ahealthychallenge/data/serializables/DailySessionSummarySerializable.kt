package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.Month
import java.time.ZonedDateTime

@Serializable
data class DailySessionSummarySerializable (
    val date: ZoneDataTimeSerializable = SerializableFactory.getZoneDataTimeSerializable(ZonedDateTime.now()),
    val totalActiveTime: DurationSerializable? = null
)