package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class SpeedRecordSerializable(
    val startTime: InstantSerializable,
    val startZoneOffset: ZoneOffsetSerializable?,
    val endTime: InstantSerializable,
    val endZoneOffset: ZoneOffsetSerializable?,
    val samples: List<SRSampleSerializable>,
)