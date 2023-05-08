/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ahealthychallenge.presentation.screen.exercisesession

import android.os.RemoteException
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.units.Length
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahealthychallenge.data.*
import com.example.ahealthychallenge.data.serializables.DailyExerciseSessionKeySerializable
import com.example.ahealthychallenge.data.serializables.DailySessionsListSerializable
import com.example.ahealthychallenge.data.serializables.SerializableFactory
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.launch
import kotlin.math.floor

class ExerciseSessionViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    private val healthConnectCompatibleApps = healthConnectManager.healthConnectCompatibleApps
    private lateinit var database: DatabaseReference
    val TAG = "ExerciseSessionViewModel"

    // TODO: manage permissions that we need
    val permissions = setOf(
        HealthPermission.getWritePermission(ExerciseSessionRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class),
        HealthPermission.getWritePermission(SpeedRecord::class),
        HealthPermission.getWritePermission(DistanceRecord::class),
        HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(SpeedRecord::class),
        HealthPermission.getReadPermission(StepsRecord::class),
    )

    var permissionsGranted = mutableStateOf(false)
        private set

    var sessionsList: MutableState<List<ExerciseSession>> = mutableStateOf(listOf())
        private set

    var dailySessionsList: MutableState<DailySessionsList> = mutableStateOf(DailySessionsList())
        private set

    var allSessions: MutableState<List<DailySessionsList>> =
        mutableStateOf(listOf(DailySessionsList()))
        private set

    var loading: MutableState<Boolean> = mutableStateOf(false)
    var refreshing: MutableState<Boolean> = mutableStateOf(false)

    var stepsList: MutableState<List<StepSession>> = mutableStateOf(listOf())
        private set

    var uiState: UiState by mutableStateOf(UiState.Uninitialized)
        private set

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    fun initialLoad() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                readExerciseSessions()
            }
        }
    }

    fun insertExerciseSession() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
                val latestStartOfSession = ZonedDateTime.now().minusMinutes(30)
                val offset = Random.nextDouble()

                // Generate random start time between the start of the day and (now - 30mins).
                val startOfSession = startOfDay.plusSeconds(
                    (Duration.between(startOfDay, latestStartOfSession).seconds * offset).toLong()
                )
                val endOfSession = startOfSession.plusMinutes(30)

                healthConnectManager.writeExerciseSession(startOfSession, endOfSession)
                readExerciseSessions()
            }
        }
    }

    //TODO: fix bug of the refreshing indicator not disappearing after the refresh
    fun deleteExerciseSession(uid: String) {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                healthConnectManager.deleteExerciseSession(uid)
                readExerciseSessions()
            }
        }
    }

    fun getUid(uid: String): String {
        return uid;
    }

    private suspend fun readExerciseSessions() {
        val today = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val now = Instant.now()
        var isTodaySessionPushed = false
        lateinit var todaySessionKey: String
        //TODO: the loading animation does not appear when there are no activities for the day
        // TODO: when there are no activities for the day the separator appear for that day
        val sessions =
            healthConnectManager.readExerciseSessions(today.toInstant(), now).map { record ->
                loading.value = true
                val packageName = record.metadata.dataOrigin.packageName
                val sessionData = healthConnectManager.readAssociatedSessionData(record.metadata.id)
                // TODO: pass the entire session data: add sessionData property in ExerciseSession
                ExerciseSession(
                    exerciseType = record.exerciseType,
                    sessionData = sessionData, // retrieve duration and distance
                    startTime = dateTimeWithOffsetOrDefault(
                        record.startTime, record.startZoneOffset
                    ),
                    endTime = dateTimeWithOffsetOrDefault(
                        record.startTime, record.startZoneOffset
                    ),
                    id = record.metadata.id,
                    sourceAppInfo = healthConnectCompatibleApps[packageName],
                    title = record.title,
                    points = getPoints(sessionData)
                )
            }
        var dailyDuration = Duration.ofSeconds(0)
        var dailyPoints = 0
        sessions.forEach {
            dailyDuration = dailyDuration.plus(it.sessionData.totalActiveTime)
            dailyPoints = dailyPoints.plus(it.points)
        }

        val dailySessionsSummary = DailySessionsSummary(today, dailyDuration, dailyPoints)
        val todaySessionsList = DailySessionsList(dailySessionsSummary, sessions)



        val todaySessionsListSerializable =
            SerializableFactory.getDailySessionsListSerializable(todaySessionsList)
        // write in the realtime database
        //TODO: add a loading animation when the data is retrieved
        database = Firebase.database.reference
        //TODO: update the database with the sessionsList of today at the position of a list of
        // DailySessionsList, and then retrieve from the database a list of DailySessionsList

        // set the database for the first time
        /*// we need to have a list of key, so we push at least 2 sessions
        // first session
        val dailySessionsSummary1 = DailySessionsSummary(today.minusDays(2), dailyDuration)
        val todaySessionsList1 = DailySessionsList(dailySessionsSummary1, sessions)
        val todaySessionsListSerializable1 =
            SerializableFactory.getDailySessionsListSerializable(todaySessionsList1)

        // push
        val ref = database.child("exerciseSessions")
            .child("userID")
            .child("exerciseSessions")

        val newChildRef1 = ref.push()
        val newKey1 = newChildRef1.key
        if (newKey1 != null) {
            // push today sessions
            newChildRef1.setValue(todaySessionsListSerializable1)

            // add the list of keys
            val newDailyExerciseSessionKey1 = DailyExerciseSessionKey(today.minusDays(2), newKey1)
            val newDailyExerciseSessionKeySerializable1 =
                SerializableFactory.getDailyExerciseSessionKeySerializable(
                    newDailyExerciseSessionKey1
                )
            database.child("exerciseSessions")
                .child("userID")
                .child("keys")
                .child("0")
                .setValue(newDailyExerciseSessionKeySerializable1)
        }

        // second session
        val dailySessionsSummary2 = DailySessionsSummary(today.minusDays(1), dailyDuration)
        val todaySessionsList2 = DailySessionsList(dailySessionsSummary2, sessions)
        val todaySessionsListSerializable2 =
            SerializableFactory.getDailySessionsListSerializable(todaySessionsList2)


        val newChildRef2 = ref.push()
        val newKey2 = newChildRef2.key
        if (newKey2 != null) {
            // push today sessions
            newChildRef2.setValue(todaySessionsListSerializable2)

            // add the list of keys
            val newDailyExerciseSessionKey2 = DailyExerciseSessionKey(today.minusDays(1), newKey2)
            val newDailyExerciseSessionKeySerializable2 =
                SerializableFactory.getDailyExerciseSessionKeySerializable(
                    newDailyExerciseSessionKey2
                )
            database.child("exerciseSessions")
                .child("userID")
                .child("keys")
                .child("1")
                .setValue(newDailyExerciseSessionKeySerializable2)
        }*/


        // set the database when data already exist
        database.child("exerciseSessions")
            .child("userID")
            .child("keys")
            .get().addOnCompleteListener {
                loading.value = true
                if (it.isSuccessful) {
                    val gti = object :
                        GenericTypeIndicator<MutableList<DailyExerciseSessionKeySerializable>>() {}
                    val dbKeys = it.result.getValue(gti)
                    val keys: List<DailyExerciseSessionKey>
                    if (dbKeys != null) {
                        //Deserialize
                        keys = dbKeys.map { key ->
                            SerializableFactory.getDailyExerciseSessionKey(key)
                        }
                        // verify if we already pushed a key today
                        val todaySKey = keys.filter {
                            it.date == today
                        }
                        if (todaySKey.size == 1) {
                            isTodaySessionPushed = true
                            todaySessionKey = todaySKey[0].key
                        }

                        //retrieve the session with the corresponding key
                        val ref = database.child("exerciseSessions")
                            .child("userID")
                            .child("exerciseSessions")

                        if (isTodaySessionPushed) {

                            ref.child(todaySessionKey)
                                .get().addOnSuccessListener {
                                    //we check is the list of session on the database is up to date
                                    val dbTodaySession =
                                        it.getValue<DailySessionsListSerializable>()
                                    if (dbTodaySession != null) {

                                        if (dbTodaySession.exerciseSessions.size < todaySessionsListSerializable.exerciseSessions.size) {
                                            // update the db with the new session list
                                            ref.child(todaySessionKey)
                                                .setValue(todaySessionsListSerializable)
                                        }
                                    }
                                }.addOnFailureListener {
                                    Log.e(TAG, "Error getting data", it)
                                }
                        } else {
                            val newChildRef = ref.push()
                            val newKey = newChildRef.key
                            if (newKey != null) {
                                // push today sessions
                                newChildRef.setValue(todaySessionsListSerializable)

                                // update the list of keys
                                val newDailyExerciseSessionKey =
                                    DailyExerciseSessionKey(today, newKey)
                                val newDailyExerciseSessionKeySerializable =
                                    SerializableFactory.getDailyExerciseSessionKeySerializable(
                                        newDailyExerciseSessionKey
                                    )
                                dbKeys.add(newDailyExerciseSessionKeySerializable)
                                database.child("exerciseSessions")
                                    .child("userID")
                                    .child("keys")
                                    .setValue(dbKeys)

                            }
                        }
                    }
                } else {
                    Log.d(TAG, it.exception?.message.toString())
                }
                loading.value = false
            }


        var myMap: Map<String, DailySessionsListSerializable>?
        var sessionsDb: List<DailySessionsList>?
        val sessionListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loading.value = true
                myMap = dataSnapshot.getValue<Map<String, DailySessionsListSerializable>?>()
                sessionsDb = myMap?.values?.toList()?.map {
                    SerializableFactory.getDailySessionsList(it)
                }
                allSessions.value = sessionsDb!!.sortedByDescending { it.dailySessionsSummary.date }
                loading.value = false
                //dailySessionsList.value = sessionsDb!![0]
                //sessionsList.value = sessionsDb!![0].exerciseSessions
                if (myMap != null) {
                    Log.d(TAG, "this is the thing: ${sessionsDb!![0]}")
                } else {
                    Log.d(TAG, "The list is null, isn't it?")
                }
                loading.value = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.child("exerciseSessions")
            .child("userID")
            .child("exerciseSessions")
            .addValueEventListener(sessionListener)
        val sevenDays = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(31)
        stepsList.value = healthConnectManager.readStepSession(sevenDays.toInstant())


        /*val ref2 = database.child("pointStats")
            .child("userID")
            .child("pieChart")

        ref2.get().addOnSuccessListener {
            //we check is the list of session on the database is up to date
            val dbMap = it.getValue<Map<String, Int>>()
            if (dbMap != null) {
                Log.d("map", "the map is $dbMap")
            }
        }*/
        refreshing.value = false
    }

    /**
     * Provides permission check and error handling for Health Connect suspend function calls.
     *
     * Permissions are checked prior to execution of [block], and if all permissions aren't granted
     * the [block] won't be executed, and [permissionsGranted] will be set to false, which will
     * result in the UI showing the permissions button.
     *
     * Where an error is caught, of the type Health Connect is known to throw, [uiState] is set to
     * [UiState.Error], which results in the snackbar being used to show the error message.
     */
    private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
        permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
        uiState = try {
            if (permissionsGranted.value) {
                block()
            }
            UiState.Done
        } catch (remoteException: RemoteException) {
            UiState.Error(remoteException)
        } catch (securityException: SecurityException) {
            UiState.Error(securityException)
        } catch (ioException: IOException) {
            UiState.Error(ioException)
        } catch (illegalStateException: IllegalStateException) {
            UiState.Error(illegalStateException)
        }
    }

    sealed class UiState {
        object Uninitialized : UiState()
        object Done : UiState()

        // A random UUID is used in each Error object to allow errors to be uniquely identified,
        // and recomposition won't result in multiple snackbars.
        data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : UiState()
    }
}

class ExerciseSessionViewModelFactory(
    private val healthConnectManager: HealthConnectManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseSessionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ExerciseSessionViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun getPoints(exerciseSessionData: ExerciseSessionData): Int {
    if (exerciseSessionData.totalDistance != null) {
        return floor(exerciseSessionData.totalDistance.inKilometers).toInt()
    } else if (exerciseSessionData.totalActiveTime != null) {
        val val1 = exerciseSessionData.totalActiveTime.seconds.toDouble()
        val val2 = Duration.ofSeconds(1).seconds.toDouble() // 1 --> 600: 1 point for every 10 min of workout
        return floor(val1 / val2).toInt() // 1 point for every 10 min of workout
    }
    return 0
}