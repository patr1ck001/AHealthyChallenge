package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class ZoneDataTimeSerializable (
    val year: Int = 1999,
    val month: Int = 6,
    val dayOfMonth: Int = 8,
    val hour: Int = 12,
    val minute: Int = 48,
    val second: Int = 43,
    val nanoOfSecond: Int = 34,
    val zone: ZoneIdSerializable = SerializableFactory.getZoneIdSerializable(ZoneId.systemDefault())
)