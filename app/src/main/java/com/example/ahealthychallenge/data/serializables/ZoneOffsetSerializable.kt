package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class ZoneOffsetSerializable(
    val totalSecond: Int = 0
)