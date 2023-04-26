package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class HRSampleSerializable(
    val time: InstantSerializable,
    @androidx.annotation.IntRange(from = 1, to = 300) val beatsPerMinute: Long
)