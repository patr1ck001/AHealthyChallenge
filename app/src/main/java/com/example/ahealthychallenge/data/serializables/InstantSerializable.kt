package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class InstantSerializable(
    val epochSecond: Long
)