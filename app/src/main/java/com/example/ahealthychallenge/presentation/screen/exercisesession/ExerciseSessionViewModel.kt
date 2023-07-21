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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.*
import com.example.ahealthychallenge.data.serializables.DailyExerciseSessionKeySerializable
import com.example.ahealthychallenge.data.serializables.DailySessionsListSerializable
import com.example.ahealthychallenge.data.serializables.InstantSerializable
import com.example.ahealthychallenge.data.serializables.LineDataSerializable
import com.example.ahealthychallenge.data.serializables.SerializableFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.math.floor
import kotlin.random.Random

class ExerciseSessionViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    private val healthConnectCompatibleApps = healthConnectManager.healthConnectCompatibleApps
    private lateinit var database: DatabaseReference
    val TAG = "ExerciseSessionViewModel"

    var uid = FirebaseAuth.getInstance().currentUser?.uid

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

    var lastInstantRead: MutableState<Instant> =
        mutableStateOf(Instant.EPOCH)
        private set
    var uiState: UiState by mutableStateOf(UiState.Uninitialized)
        private set

    val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

    fun initialLoad() {
        viewModelScope.launch {
            tryWithPermissionsCheck {
                readLastInstantInDb()
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
        database = Firebase.database.reference
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
                val newPoints = getPoints(sessionData)
                val exerciseType = record.exerciseType
                // TODO: pass the entire session data: add sessionData property in ExerciseSession
                ExerciseSession(
                    exerciseType = exerciseType,
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
                    points = newPoints
                )
            }
        Log.d("offset", "the last value read is: ${lastInstantRead.value}")

        if (lastInstantRead.value !== Instant.EPOCH) {
            healthConnectManager.readExerciseSessions(
                lastInstantRead.value,
                now
            ).map { record ->
                val sessionData = healthConnectManager.readAssociatedSessionData(record.metadata.id)
                val newPoints = getPoints(sessionData)
                val exerciseType = record.exerciseType
                writePieDataOnTheDb(exerciseType, newPoints)
                writeCurveLineDataOnTheDb(newPoints)
                val pathString = when(exerciseType) {
                    8 -> "bikingLineData"
                    56 -> "runningLineData"
                    79 -> "walkingLineData"
                    else -> "workoutLineData"
                }
                writeExerciseLineDataOnTheDb(newPoints, pathString)
            }
            writeLastInstantInDb(SerializableFactory.getInstantSerializable(Instant.now()))
        }

        /*val curveLineData = listOf(
            LineDataSerializable(1, 3F),
            LineDataSerializable(2, 15F),
            LineDataSerializable(3, 9F),
            LineDataSerializable(4, 3F),
            LineDataSerializable(5, 34F),
            LineDataSerializable(6, 23F),
            LineDataSerializable(7, 19F),
            LineDataSerializable(8, 20F),
            LineDataSerializable(9, 15F),
            LineDataSerializable(10, 17F),
            LineDataSerializable(11, 17F),
            LineDataSerializable(12, 13F),
            LineDataSerializable(13, 20F),
            LineDataSerializable(14, 22F),
            LineDataSerializable(15, 23F),
            LineDataSerializable(16, 10F),
            LineDataSerializable(17, 14F),
            LineDataSerializable(18, 23F),
        )

        database.child("pointStats")
            .child("userID")
            .child("workoutLineData")
            .setValue(curveLineData)

        Log.d("curve", "success")
        val refer = database.child("pointStats")
            .child("userID")
            .child("workoutLineData")

        refer.get().addOnSuccessListener {
            val curveLineDataDb = it.getValue<List<LineDataSerializable>>()
            if (curveLineDataDb != null) {
                val curveLineList = curveLineDataDb.map { lineData ->
                    SerializableFactory.getLineData(lineData)
                }
                Log.d("curve", "the data are: $curveLineList")
            }
        }*/
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
            .child(uid!!)
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
                            .child(uid!!)
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
                                    .child(uid!!)
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
            .child(uid!!)
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

    fun readLastInstantInDb() {
        val database = Firebase.database.reference

        val refer = database.child("pointStats")
            .child(uid!!)
            .child("pieChart")
            .child("lastInstant")

        refer.get().addOnSuccessListener {
            val lastInstantDb = it.getValue<InstantSerializable>()
            val lastInstant: Instant?
            if (lastInstantDb != null) {
                lastInstant = SerializableFactory.getInstant(lastInstantDb)
                lastInstantRead.value = lastInstant
            }
        }
    }

    fun writeLastInstantInDb(instantSerializable: InstantSerializable) {
        val database = Firebase.database.reference
        database.child("pointStats")
            .child(uid!!)
            .child("pieChart")
            .child("lastInstant")
            .setValue(instantSerializable)
    }

    fun writeCurveLineDataOnTheDb(newPoints: Int) {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child(uid!!)
            .child("curveLine")
            .child("curveLineData")

        Log.d("curve", "1")
        refer.get().addOnSuccessListener {
            Log.d("curve", "3")
            Log.d("curve", "before")
            val dayOfMonth = ZonedDateTime.now().dayOfMonth
            var isTodayPresent = false
            val curveLineDataDb = it.getValue<MutableList<LineDataSerializable>>()
            Log.d("curve", "deserialized: $curveLineDataDb")
            if (curveLineDataDb != null) {
                curveLineDataDb.map { lineData ->
                    if (lineData.xvalue == dayOfMonth) {
                        isTodayPresent = true
                        lineData.yvalue = lineData.yvalue + newPoints.toFloat()
                    } else {
                        lineData
                    }
                }
                if (!isTodayPresent) {
                    curveLineDataDb.add(LineDataSerializable(dayOfMonth, newPoints.toFloat()))
                }
                Log.d("curve", "about to serialize: $curveLineDataDb")

                refer.setValue(curveLineDataDb)
            }
        }
        Log.d("curve", "2")

    }

    fun writeExerciseLineDataOnTheDb(newPoints: Int, pathString: String) {
        database = Firebase.database.reference
        val refer = database
            .child("pointStats")
            .child(uid!!)
            .child(pathString)

        Log.d("lineData", "1")
        refer.get().addOnSuccessListener {
            Log.d("lineData", "3")
            Log.d("lineData", "before")
            val dayOfMonth = ZonedDateTime.now().dayOfMonth
            var isTodayPresent = false
            val lineDataDb = it.getValue<MutableList<LineDataSerializable>>()
            Log.d("lineData", "deserialized: $lineDataDb")
            if (lineDataDb != null) {
                lineDataDb.map { lineData ->
                    if (lineData.xvalue == dayOfMonth) {
                        isTodayPresent = true
                        lineData.yvalue = lineData.yvalue + newPoints.toFloat()
                    } else {
                        lineData
                    }
                }
                if (!isTodayPresent) {
                    lineDataDb.add(LineDataSerializable(dayOfMonth, newPoints.toFloat()))
                }
                Log.d("lineData", "about to serialize: $lineDataDb")

                refer.setValue(lineDataDb)
            }
        }
        Log.d("lineData", "2")

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
        val val2 =
            Duration.ofSeconds(1).seconds.toDouble() // 1 --> 600: 1 point for every 10 min of workout
        return floor(val1 / val2).toInt() // 1 point for every 10 min of workout
    }
    return 0
}

fun writePieDataOnTheDb(exerciseType: Int, newPoints: Int) {
    val database = Firebase.database.reference
    val ref = database.child("pointStats")
        .child(FirebaseAuth.getInstance().currentUser?.uid!!)
        .child("pieChart")
        .child("pieChartData")

    when (exerciseType) {
        56 -> {
            ref.child("running").get().addOnSuccessListener {
                val runningPoints = it.getValue<Int>()
                if (runningPoints != null) {
                    ref.child("running").setValue(runningPoints + newPoints)
                }
            }
        }

        79 -> {
            ref.child("walking").get().addOnSuccessListener {
                val walkingPoints = it.getValue<Int>()
                if (walkingPoints != null) {
                    ref.child("walking").setValue(walkingPoints + newPoints)
                }
            }
        }

        8 -> {
            ref.child("cycling").get().addOnSuccessListener {
                val cyclingPoints = it.getValue<Int>()
                if (cyclingPoints != null) {
                    ref.child("cycling").setValue(cyclingPoints + newPoints)
                }
            }
        }

        else -> {
            ref.child("workout").get().addOnSuccessListener {
                val workoutPoints = it.getValue<Int>()
                if (workoutPoints != null) {
                    ref.child("workout").setValue(workoutPoints + newPoints)
                }
            }
        }
    }
    Log.d("write", "write in db $exerciseType")
}

