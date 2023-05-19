package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class LineDataSerializable(
    val xvalue: Int = 0,
    var yvalue: Float = 1F
)