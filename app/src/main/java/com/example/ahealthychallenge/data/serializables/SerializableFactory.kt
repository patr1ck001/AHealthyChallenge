package com.example.ahealthychallenge.data.serializables

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Velocity
import com.example.ahealthychallenge.data.*
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

object SerializableFactory {
    fun getDuration(durationSerializable: DurationSerializable): Duration {
        return Duration.ofSeconds(durationSerializable.durationInSeconds)
    }

    fun getDurationSerializable(duration: Duration?): DurationSerializable? {
        return duration?.let { DurationSerializable(it.seconds) }
    }

    fun getInstant(instantSerializable: InstantSerializable): Instant {
        return Instant.ofEpochSecond(instantSerializable.epochSecond)
    }

    fun getInstantSerializable(instant: Instant): InstantSerializable {
        return InstantSerializable(instant.epochSecond)
    }

    fun getZoneOffset(zoneOffsetSerializable: ZoneOffsetSerializable): ZoneOffset {
        return ZoneOffset.ofTotalSeconds(zoneOffsetSerializable.totalSecond)
    }

    fun getZoneOffsetSerializable(zoneOffset: ZoneOffset?): ZoneOffsetSerializable? {
        return zoneOffset?.let { ZoneOffsetSerializable(it.totalSeconds) }
    }

    //TODO: make enum out of this constant
    fun getLength(lengthSerializable: LengthSerializable): Length {
        return when (lengthSerializable.type) {
            "METERS" -> Length.meters(lengthSerializable.value)
            "KILOMETERS" -> Length.kilometers(lengthSerializable.value)
            "MILES" -> Length.miles(lengthSerializable.value)
            "INCHES" -> Length.inches(lengthSerializable.value)
            "FEET" -> Length.feet(lengthSerializable.value)
            else -> Length.meters(0.0)

        }
    }

    fun getLengthSerializable(length: Length?): LengthSerializable? {
        return length?.let { LengthSerializable(it.inKilometers, "KILOMETERS") }
    }


    //TODO: make enum out of this constant
    fun getEnergy(energySerializable: EnergySerializable): Energy {
        return when (energySerializable.type) {
            "CALORIES" -> Energy.calories(energySerializable.value)
            "KILOCALORIES" -> Energy.kilocalories(energySerializable.value)
            "JOULES" -> Energy.joules(energySerializable.value)
            "KILOJOULES" -> Energy.kilojoules(energySerializable.value)
            else -> Energy.calories(0.0)

        }
    }

    fun getEnergySerializable(energy: Energy?): EnergySerializable? {
        return energy?.let { EnergySerializable(it.inKilocalories, "KILOCALORIES") }
    }

    //TODO: make enum out of this constant
    fun getVelocity(velocitySerializable: VelocitySerializable): Velocity {
        return when (velocitySerializable.type) {
            "METERS_PER_SECOND" -> Velocity.metersPerSecond(velocitySerializable.value)
            "KILOMETERS_PER_HOUR" -> Velocity.kilometersPerHour(velocitySerializable.value)
            "MILES_PER_HOUR" -> Velocity.milesPerHour(velocitySerializable.value)
            else -> Velocity.milesPerHour(0.0)
        }
    }

    fun getVelocitySerializable(velocity: Velocity): VelocitySerializable {
        return VelocitySerializable(
            velocity.inKilometersPerHour,
            "KILOMETERS_PER_HOUR"
        )
    }

    fun getHRSample(sampleSerializable: HRSampleSerializable): HeartRateRecord.Sample {
        return HeartRateRecord.Sample(
            getInstant(sampleSerializable.time),
            sampleSerializable.beatsPerMinute
        )
    }

    fun getHRSampleSerializable(sample: HeartRateRecord.Sample): HRSampleSerializable {
        return HRSampleSerializable(
            InstantSerializable(sample.time.epochSecond),
            sample.beatsPerMinute
        )
    }


    fun getSRSample(sampleSRSerializable: SRSampleSerializable): SpeedRecord.Sample {
        return SpeedRecord.Sample(
            getInstant(sampleSRSerializable.time),
            getVelocity(sampleSRSerializable.speed)
        )
    }

    fun getSRSampleSerializable(sample: SpeedRecord.Sample): SRSampleSerializable {
        return SRSampleSerializable(
            getInstantSerializable(sample.time),
            getVelocitySerializable(sample.speed)
        )
    }

    fun getHeartRateRecord(heartRateRecordSerializable: HeartRateRecordSerializable): HeartRateRecord {
        return HeartRateRecord(
            getInstant(heartRateRecordSerializable.startTime),
            heartRateRecordSerializable.startZoneOffset?.let { getZoneOffset(it) },
            getInstant(heartRateRecordSerializable.endTime),
            heartRateRecordSerializable.endZoneOffset?.let { getZoneOffset(it) },
            heartRateRecordSerializable.samples.map { sample ->
                getHRSample(sample)
            }
        )
    }

    fun getHeartRateRecordSerializable(heartRateRecord: HeartRateRecord): HeartRateRecordSerializable {
        return HeartRateRecordSerializable(
            getInstantSerializable(heartRateRecord.startTime),
            getZoneOffsetSerializable(heartRateRecord.startZoneOffset),
            getInstantSerializable(heartRateRecord.endTime),
            getZoneOffsetSerializable(heartRateRecord.endZoneOffset),
            heartRateRecord.samples.map { sample ->
                getHRSampleSerializable(sample)
            }
        )
    }

    fun getSpeedRecord(speedRecordSerializable: SpeedRecordSerializable): SpeedRecord {
        return SpeedRecord(
            getInstant(speedRecordSerializable.startTime),
            speedRecordSerializable.startZoneOffset?.let { getZoneOffset(it) },
            getInstant(speedRecordSerializable.endTime),
            speedRecordSerializable.endZoneOffset?.let { getZoneOffset(it) },
            speedRecordSerializable.samples.map { sample ->
                getSRSample(sample)
            }
        )
    }

    fun getSpeedRecordSerializable(speedRecord: SpeedRecord): SpeedRecordSerializable {
        return SpeedRecordSerializable(
            getInstantSerializable(speedRecord.startTime),
            getZoneOffsetSerializable(speedRecord.startZoneOffset),
            getInstantSerializable(speedRecord.endTime),
            getZoneOffsetSerializable(speedRecord.endZoneOffset),
            speedRecord.samples.map { sample ->
                getSRSampleSerializable(sample)
            }
        )
    }

    fun getExerciseSessionData(exerciseSessionDataSerializable: ExerciseSessionDataSerializable): ExerciseSessionData {
        return ExerciseSessionData(
            exerciseSessionDataSerializable.uid,
            exerciseSessionDataSerializable.totalActiveTime?.let { getDuration(it) },
            exerciseSessionDataSerializable.totalSteps,
            exerciseSessionDataSerializable.totalDistance?.let { getLength(it) },
            exerciseSessionDataSerializable.totalEnergyBurned?.let { getEnergy(it) },
            exerciseSessionDataSerializable.minHeartRate,
            exerciseSessionDataSerializable.maxHeartRate,
            exerciseSessionDataSerializable.avgHeartRate,
            exerciseSessionDataSerializable.heartRateSeries.map { hr ->
                getHeartRateRecord(hr)
            },
            exerciseSessionDataSerializable.minSpeed?.let { getVelocity(it) },
            exerciseSessionDataSerializable.maxSpeed?.let { getVelocity(it) },
            exerciseSessionDataSerializable.avgSpeed?.let { getVelocity(it) },
            exerciseSessionDataSerializable.speedRecord.map { sr ->
                getSpeedRecord(sr)
            }
        )
    }

    fun getExerciseSessionDataSerializable(exerciseSessionData: ExerciseSessionData): ExerciseSessionDataSerializable {
        return ExerciseSessionDataSerializable(
            exerciseSessionData.uid,
            getDurationSerializable(exerciseSessionData.totalActiveTime),
            exerciseSessionData.totalSteps,
            getLengthSerializable(exerciseSessionData.totalDistance),
            getEnergySerializable(exerciseSessionData.totalEnergyBurned),
            exerciseSessionData.minHeartRate,
            exerciseSessionData.maxHeartRate,
            exerciseSessionData.avgHeartRate,
            exerciseSessionData.heartRateSeries.map { hr ->
                getHeartRateRecordSerializable(hr)
            },
            exerciseSessionData.minSpeed?.let { getVelocitySerializable(it) },
            exerciseSessionData.maxSpeed?.let { getVelocitySerializable(it) },
            exerciseSessionData.avgSpeed?.let { getVelocitySerializable(it) },
            exerciseSessionData.speedRecord.map { sr ->
                getSpeedRecordSerializable(sr)
            }
        )
    }

    fun getZoneId(zoneIdSerializable: ZoneIdSerializable): ZoneId {
        return ZoneId.of(zoneIdSerializable.Id)
    }

    fun getZoneIdSerializable(zoneId: ZoneId): ZoneIdSerializable {
        return ZoneIdSerializable(zoneId.id)
    }

    fun getZoneDataTime(zoneDataTimeSerializable: ZoneDataTimeSerializable): ZonedDateTime {
        return ZonedDateTime.of(
            zoneDataTimeSerializable.year,
            zoneDataTimeSerializable.month,
            zoneDataTimeSerializable.dayOfMonth,
            zoneDataTimeSerializable.hour,
            zoneDataTimeSerializable.minute,
            zoneDataTimeSerializable.second,
            zoneDataTimeSerializable.nanoOfSecond,
            getZoneId(zoneDataTimeSerializable.zone)
        )
    }

    fun getZoneDataTimeSerializable(zonedDateTime: ZonedDateTime): ZoneDataTimeSerializable {
        return ZoneDataTimeSerializable(
            zonedDateTime.year,
            zonedDateTime.monthValue,
            zonedDateTime.dayOfMonth,
            zonedDateTime.hour,
            zonedDateTime.minute,
            zonedDateTime.second,
            zonedDateTime.nano,
            getZoneIdSerializable(zonedDateTime.zone)
        )
    }

    fun getExerciseSession(exerciseSessionSerializable: ExerciseSessionSerializable): ExerciseSession {
        return ExerciseSession(
            getExerciseSessionData(exerciseSessionSerializable.sessionData),
            exerciseSessionSerializable.exerciseType,
            getZoneDataTime(exerciseSessionSerializable.startTime),
            getZoneDataTime(exerciseSessionSerializable.endTime),
            exerciseSessionSerializable.id,
            exerciseSessionSerializable.title,
            exerciseSessionSerializable.sourceAppInfo
        )
    }

    fun getExerciseSessionSerializable(exerciseSession: ExerciseSession): ExerciseSessionSerializable {
        return ExerciseSessionSerializable(
            getExerciseSessionDataSerializable(exerciseSession.sessionData),
            exerciseSession.exerciseType,
            getZoneDataTimeSerializable(exerciseSession.startTime),
            getZoneDataTimeSerializable(exerciseSession.endTime),
            exerciseSession.id,
            exerciseSession.title,
            exerciseSession.sourceAppInfo?.let {
                HealthConnectAppInfo(
                    it.packageName,
                    exerciseSession.sourceAppInfo.appLabel,
                    null
                )
            }
        )
    }

    fun getDailySessionSummarySerializable(dailySessionsSummary: DailySessionsSummary): DailySessionSummarySerializable {
        return DailySessionSummarySerializable(
            getZoneDataTimeSerializable(dailySessionsSummary.date),
            getDurationSerializable(dailySessionsSummary.totalActiveTime)
        )
    }

    fun getDailySessionSummary(dailySessionSummarySerializable: DailySessionSummarySerializable): DailySessionsSummary {
        return DailySessionsSummary(
            getZoneDataTime(dailySessionSummarySerializable.date),
            dailySessionSummarySerializable.totalActiveTime?.let { getDuration(it) }
        )
    }

    fun getDailySessionsListSerializable(dailySessionsList: DailySessionsList): DailySessionsListSerializable{
        return DailySessionsListSerializable(
            getDailySessionSummarySerializable(dailySessionsList.dailySessionsSummary),
            dailySessionsList.exerciseSessions.map {
                session -> getExerciseSessionSerializable(session)
            }
        )
    }

    fun getDailySessionsList(dailySessionsListSerializable: DailySessionsListSerializable): DailySessionsList{
        return DailySessionsList(
            getDailySessionSummary(dailySessionsListSerializable.dailySessionsSummary),
            dailySessionsListSerializable.exerciseSessions.map {
                    session -> getExerciseSession(session)
            }
        )
    }

    fun getDailyExerciseSessionKeySerializable(dailyExerciseSessionKey: DailyExerciseSessionKey) : DailyExerciseSessionKeySerializable{
        return DailyExerciseSessionKeySerializable(
            getZoneDataTimeSerializable(dailyExerciseSessionKey.date),
            dailyExerciseSessionKey.key
        )
    }

    fun getDailyExerciseSessionKey(dailyExerciseSessionKeySerializable: DailyExerciseSessionKeySerializable) : DailyExerciseSessionKey{
        return DailyExerciseSessionKey(
            getZoneDataTime(dailyExerciseSessionKeySerializable.date),
            dailyExerciseSessionKeySerializable.key
        )
    }


}