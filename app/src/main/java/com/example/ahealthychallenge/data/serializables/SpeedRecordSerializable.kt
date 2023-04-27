package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class SpeedRecordSerializable(
    val startTime: InstantSerializable = SerializableFactory.getInstantSerializable(Instant.now()),
    val startZoneOffset: ZoneOffsetSerializable? = null,
    val endTime: InstantSerializable = SerializableFactory.getInstantSerializable(Instant.now()),
    val endZoneOffset: ZoneOffsetSerializable? = null,
    val samples: List<SRSampleSerializable> = listOf(),
)