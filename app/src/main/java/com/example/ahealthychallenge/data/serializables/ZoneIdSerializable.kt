package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class ZoneIdSerializable (
    val Id: String = ZoneId.systemDefault().id
)