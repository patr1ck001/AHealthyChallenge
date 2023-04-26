package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class ZoneDataTimeSerializable (
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nanoOfSecond: Int,
    val zone: ZoneIdSerializable
)