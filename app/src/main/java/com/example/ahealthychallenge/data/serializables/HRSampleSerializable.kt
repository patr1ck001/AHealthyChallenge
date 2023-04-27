package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class HRSampleSerializable(
    val time: InstantSerializable = SerializableFactory.getInstantSerializable(Instant.now()),
    @androidx.annotation.IntRange(from = 1, to = 300) val beatsPerMinute: Long
)