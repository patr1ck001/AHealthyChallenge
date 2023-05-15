package com.example.ahealthychallenge.data

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Velocity
import com.example.ahealthychallenge.data.serializables.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


fun main() {
    val json = Json {
        prettyPrint = true
    }

    // test duration
    val duration = Duration.ofSeconds(720)
    //println("This is the original duration: $duration")
    val durationSerializable = SerializableFactory.getDurationSerializable(duration)
    val jsonString = json.encodeToString(durationSerializable)
    //println("ENCODED: $jsonString")
    val jsonElement = json.parseToJsonElement(jsonString)
    val durationDeserializable = json.decodeFromJsonElement<DurationSerializable>(jsonElement)
    val finalDuration = SerializableFactory.getDuration(durationDeserializable)
    //println("DECODED: $finalDuration")

    val sessionData = ExerciseSessionDataSerializable("A12s3", durationSerializable)
    val jsonSessionData = json.encodeToString(sessionData)
    //println("ENCODED: $jsonSessionData")

    //test length
    val length = Length.kilometers(10.0)
    //println("This is the original length: $length")
    val lengthSerializable = SerializableFactory.getLengthSerializable(length)
    val jsonLength = json.encodeToString(lengthSerializable)
    //println("ENCODED: $jsonLength")
    val jsonLengthElement = json.parseToJsonElement(jsonLength)
    val lengthDeserializable = json.decodeFromJsonElement<LengthSerializable>(jsonLengthElement)
    val finalLength = SerializableFactory.getLength(lengthDeserializable)
    //println("DECODED: $finalLength")

    //test energy
    val energy = Energy.kilocalories(200.0)
    //println("This is the original length: $energy")
    val energySerializable = SerializableFactory.getEnergySerializable(energy)
    val jsonEnergy = json.encodeToString(energySerializable)
    //println("ENCODED: $jsonEnergy")
    val jsonEnergyElement = json.parseToJsonElement(jsonEnergy)
    val energyDeserializable = json.decodeFromJsonElement<EnergySerializable>(jsonEnergyElement)
    val finalEnergy = SerializableFactory.getEnergy(energyDeserializable)
    //println("DECODED: $finalEnergy")

    // test instant
    val instant = Instant.ofEpochSecond(72000000000)
    //println("This is the original duration: $instant")
    val instantSerializable = SerializableFactory.getInstantSerializable(instant)
    val jsonInstantString = json.encodeToString(instantSerializable)
    //println("ENCODED: $jsonInstantString")
    val jsonInstantElement = json.parseToJsonElement(jsonInstantString)
    val instantDeserializable = json.decodeFromJsonElement<InstantSerializable>(jsonInstantElement)
    val finalInstant = SerializableFactory.getInstant(instantDeserializable)
    //println("DECODED: $finalInstant")

    // test ZoneOffset
    val zoneOffset = ZoneOffset.ofTotalSeconds(2000)
    //println("This is the original duration: $zoneOffset")
    val zoneOffsetSerializable = SerializableFactory.getZoneOffsetSerializable(zoneOffset)
    val jsonZoneOffsetString = json.encodeToString(zoneOffsetSerializable)
    //println("ENCODED: $jsonZoneOffsetString")
    val jsonZoneOffsetElement = json.parseToJsonElement(jsonZoneOffsetString)
    val zoneOffsetDeserializable =
        json.decodeFromJsonElement<ZoneOffsetSerializable>(jsonZoneOffsetElement)
    val finalZoneOffset = SerializableFactory.getZoneOffset(zoneOffsetDeserializable)
    //println("DECODED: $finalZoneOffset")

    // test Sample
    val sample = HeartRateRecord.Sample(instant, 100)
    //println("This is the original duration: $sample")
    val sampleSerializable = SerializableFactory.getHRSampleSerializable(sample)
    val jsonSampleString = json.encodeToString(sampleSerializable)
    //println("ENCODED: $jsonSampleString")
    val jsonSampleElement = json.parseToJsonElement(jsonSampleString)
    val sampleDeserializable = json.decodeFromJsonElement<HRSampleSerializable>(jsonSampleElement)
    val finalSample = SerializableFactory.getHRSample(sampleDeserializable)
    //println("DECODED: $finalSample")

    // test heartRecord
    val heartRateRecord =
        HeartRateRecord(instant, zoneOffset, instant, zoneOffset, listOf(sample, sample))
    //println("This is the original duration: $heartRateRecord")
    val heartRateRecordSerializable = SerializableFactory.getHeartRateRecordSerializable(heartRateRecord)
    val jsonHRString = json.encodeToString(heartRateRecordSerializable)
    //println("ENCODED: $jsonHRString")
    val jsonHRElement = json.parseToJsonElement(jsonHRString)
    val hRDeserializable = json.decodeFromJsonElement<HeartRateRecordSerializable>(jsonHRElement)
    val finalHR = SerializableFactory.getHeartRateRecord(hRDeserializable)
    //println("DECODED: $finalHR")


    //test velocity
    val velocity = Velocity.kilometersPerHour(12.0)
    //println("This is the original length: $velocity")
    val velocitySerializable = SerializableFactory.getVelocitySerializable(velocity)
    val jsonVelocity = json.encodeToString(velocitySerializable)
    //println("ENCODED: $jsonVelocity")
    val jsonVelocityElement = json.parseToJsonElement(jsonVelocity)
    val velocityDeserializable =
        json.decodeFromJsonElement<VelocitySerializable>(jsonVelocityElement)
    val finalVelocity = SerializableFactory.getVelocity(velocityDeserializable)
    //println("DECODED: $finalVelocity")

    // test speedRecord
    val srSample = SpeedRecord.Sample(instant, velocity)
    val speedRecord =
        SpeedRecord(instant, zoneOffset, instant, zoneOffset, listOf(srSample, srSample))
    //println("This is the original duration: $speedRecord")
    val speedRecordSerializable = SerializableFactory.getSpeedRecordSerializable(speedRecord)
    val jsonSRString = json.encodeToString(speedRecordSerializable)
    //println("ENCODED: $jsonSRString")
    val jsonSRElement = json.parseToJsonElement(jsonSRString)
    val sRDeserializable = json.decodeFromJsonElement<SpeedRecordSerializable>(jsonSRElement)
    val finalSR = SerializableFactory.getSpeedRecord(sRDeserializable)
    //println("DECODED: $finalSR")

    // test exerciseSessionData
    val exerciseSessionData = ExerciseSessionData(
        "uid",
        duration,
        6000,
        length,
        energy,
        1000,
        2000,
        1500,
        listOf(heartRateRecord, heartRateRecord),
        velocity,
        velocity,
        velocity,
        listOf(speedRecord, speedRecord)
    )

    //println("This is the original session data:\n $exerciseSessionData")
    val exerciseSessionDataSerializable = SerializableFactory.getExerciseSessionDataSerializable(exerciseSessionData)
    val jsonESString = json.encodeToString(exerciseSessionDataSerializable)
    //println("ENCODED: $jsonESString")
    val jsonSEElement = json.parseToJsonElement(jsonESString)
    val sEDeserializable = json.decodeFromJsonElement<ExerciseSessionDataSerializable>(jsonSEElement)
    val finalSE = SerializableFactory.getExerciseSessionData(sEDeserializable)
    //println("DECODED: $finalSE")

    val zoneDateTime = ZonedDateTime.now()
    //println("this is the zone id: ${zoneDateTime.zone}")
    val zoneIdSerializable = SerializableFactory.getZoneIdSerializable(zoneDateTime.zone)
    val jsonZoneIdString = json.encodeToString(zoneIdSerializable)
    //println("ENCODED: $jsonZoneIdString")
    val jsonZoneIdElement = json.parseToJsonElement(jsonZoneIdString)
    val zoneIdDeserializable = json.decodeFromJsonElement<ZoneIdSerializable>(jsonZoneIdElement)
    val finalZoneId = SerializableFactory.getZoneId(zoneIdDeserializable)
    //println("DECODED: $finalZoneId")

    //println("this is the zone time is: $zoneDateTime")
    val zoneSerializable = SerializableFactory.getZoneDataTimeSerializable(zoneDateTime)
    val jsonZoneString = json.encodeToString(zoneSerializable)
    //println("ENCODED: $jsonZoneString")

    val jsonZoneElement = json.parseToJsonElement(jsonZoneString)
    val zoneDeserializable = json.decodeFromJsonElement<ZoneDataTimeSerializable>(jsonZoneElement)
    val finalZone = SerializableFactory.getZoneDataTime(zoneDeserializable)
    //println("DECODED: $finalZone")

    // test exerciseSession
    val exerciseSession = ExerciseSession(
        exerciseSessionData,
        56,
        zoneDateTime,
        zoneDateTime,
        "id",
        "title",
        HealthConnectAppInfo(
            "name",
            "appLabel"
        ),
        2
    )
    //println("this is the session: $exerciseSession")

    val exerciseSessionSerializable = SerializableFactory.getExerciseSessionSerializable(exerciseSession)
    val jsonSessionString = json.encodeToString(exerciseSessionSerializable)
   println("ENCODED: $jsonSessionString")

    val jsonSessionElement = json.parseToJsonElement(jsonSessionString)
    val sessionDeserializable = json.decodeFromJsonElement<ExerciseSessionSerializable>(jsonSessionElement)
    val finalSession = SerializableFactory.getExerciseSession(sessionDeserializable)
   //println("DECODED: $finalSession")

    val  today = LocalDate.now()
    val midnight = LocalTime.MIDNIGHT
    val todayMidnight = LocalDateTime.of(today, midnight)
    //println("this it today instant: ${ZonedDateTime.now()} and midnight $todayMidnight")
}

