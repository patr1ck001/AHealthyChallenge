package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class DailyExerciseSessionKeySerializable (
    val date: ZoneDataTimeSerializable = SerializableFactory.getZoneDataTimeSerializable(ZonedDateTime.now()),
    val key: String = "no_key"
)