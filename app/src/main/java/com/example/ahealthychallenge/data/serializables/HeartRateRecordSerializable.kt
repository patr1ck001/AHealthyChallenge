package com.example.ahealthychallenge.data.serializables


import kotlinx.serialization.Serializable

@Serializable
data class HeartRateRecordSerializable(
    val startTime: InstantSerializable,
    val startZoneOffset: ZoneOffsetSerializable?,
    val endTime: InstantSerializable,
    val endZoneOffset: ZoneOffsetSerializable?,
    val samples: List<HRSampleSerializable>
)