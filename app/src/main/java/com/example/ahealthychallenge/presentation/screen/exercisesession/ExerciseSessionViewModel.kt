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
                val pathString = when (exerciseType) {
                    8 -> "bikingLineData"
                    56 -> "runningLineData"
                    79 -> "walkingLineData"
                    else -> "workoutLineData"
                }
                writeExerciseLineDataOnTheDb(newPoints, pathString)
            }
            writeLastInstantInDb(SerializableFactory.getInstantSerializable(Instant.now()))
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

        database.child("exerciseSessions")
            .child(uid!!)
            .child("keys")
            .get().addOnSuccessListener {
                loading.value = true
                val dbKeys = it.getValue<MutableList<DailyExerciseSessionKeySerializable>>()
                if (dbKeys != null) { //*****************if the user is already present in the database*****************
                    Log.d("dbKey", "the key of the db are: $dbKeys")
                    //Deserialize
                    val keys: List<DailyExerciseSessionKey> = dbKeys.map { key ->
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
                            }
                        loading.value = false
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

                } else { //*****************if the user is not present in the database*****************
                    Log.d("dbKey", "add a new branch in the db")
                    val newUserRef = database.child("exerciseSessions").child(uid!!)

                    // push a new key for exercises session of the new user
                    val newUserExerciseRef = newUserRef.child("exerciseSessions").push()
                    val newKey = newUserExerciseRef.key
                    if (newKey != null) {
                        // push today sessions
                        newUserExerciseRef.setValue(todaySessionsListSerializable)

                        // update the list of keys
                        val newDailyExerciseSessionKey =
                            DailyExerciseSessionKey(today, newKey)
                        val newDailyExerciseSessionKeySerializable =
                            SerializableFactory.getDailyExerciseSessionKeySerializable(
                                newDailyExerciseSessionKey
                            )
                        val userDbKeys = mutableListOf(
                            newDailyExerciseSessionKeySerializable
                        )
                        newUserRef.child("keys").setValue(userDbKeys)
                    }
                }
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

                if (sessionsDb != null) {
                    allSessions.value =
                        sessionsDb!!.sortedByDescending { it.dailySessionsSummary.date }

                }
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
        val startOfDayInstant = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).toInstant()

        val refer = database.child("pointStats")
            .child(uid!!)
            .child("pieChart")
            .child("lastInstant")

        refer.get().addOnSuccessListener {
            if (it.exists()) {
                val lastInstantDb = it.getValue<InstantSerializable>()
                val lastInstant: Instant?
                if (lastInstantDb != null) {
                    lastInstant = SerializableFactory.getInstant(lastInstantDb)
                    lastInstantRead.value = lastInstant
                }
            } else {
                refer.setValue(startOfDayInstant)
                lastInstantRead.value = startOfDayInstant
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

    private fun writeCurveLineDataOnTheDb(newPoints: Int) {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child(uid!!)
            .child("curveLine")
            .child("curveLineData")

        refer.get().addOnSuccessListener {
            val dayOfMonth = ZonedDateTime.now().dayOfMonth
            var isTodayPresent = false
            if (it.exists()) {
                val curveLineDataDb = it.getValue<MutableList<LineDataSerializable>>()
                Log.d("debugExercise","the curve is: $curveLineDataDb")
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
                    Log.d("lineDataCurve", "about to serialize: $curveLineDataDb")

                    refer.setValue(curveLineDataDb)
                }
            } else {
                val list = mutableListOf(
                    LineDataSerializable(
                        xvalue = dayOfMonth,
                        yvalue = newPoints.toFloat()
                    )
                )
                refer.setValue(list)

            }

        }
        Log.d("curve", "2")

    }

    private fun writeExerciseLineDataOnTheDb(newPoints: Int, pathString: String) {
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
            if (it.exists()) {
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

            } else {
                val list = mutableListOf(
                    LineDataSerializable(
                        xvalue = dayOfMonth,
                        yvalue = newPoints.toFloat()
                    )
                )
                refer.setValue(list)

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
    /*if (exerciseSessionData.totalDistance != null) {
        return floor(exerciseSessionData.totalDistance.inKilometers).toInt()
    } else*/
    if (exerciseSessionData.totalActiveTime != null) {
        val val1 = exerciseSessionData.totalActiveTime.seconds.toDouble()
        val val2 =
            Duration.ofSeconds(1).seconds.toDouble() // 1 --> 600: 1 point for every 10 min of workout
        return floor(val1 / val2).toInt() // 1 point for every 10 min of workout
    }
    return 0
}

fun writePieDataOnTheDb(exerciseType: Int, newPoints: Int) {
    val firebase = FirebaseDatabase.getInstance().getReference("Users")
    val leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard")
    val database = Firebase.database.reference
    val ref = database.child("pointStats")
        .child(FirebaseAuth.getInstance().currentUser?.uid!!)
        .child("pieChart")
        .child("pieChartData")

    firebase.child(FirebaseAuth.getInstance().currentUser?.uid!!).get()
        .addOnSuccessListener { user ->
            val currentUsername = user.value.toString()

            fun updateFriends() {
                val pointsDataListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val pointsSheet = snapshot.getValue<UserPointsSheet>()
                        leaderboardRef.child(currentUsername).child("friends").get()
                            .addOnSuccessListener { list ->
                                if (list.exists()) {
                                    val listFriend = list.getValue<MutableList<Friend>>()
                                    if (listFriend != null) {
                                        for (friend in listFriend) {
                                            leaderboardRef.child(friend.username.toString())
                                                .child("friends").get()
                                                .addOnSuccessListener { list2 ->
                                                    if (list2.exists()) {
                                                        val listFriend2 =
                                                            list2.getValue<MutableList<Friend>>()
                                                        if (listFriend2 != null) {
                                                            for (friend2 in listFriend2) {
                                                                if (friend2.username.toString() == currentUsername) {
                                                                    friend2.pointsSheet =
                                                                        pointsSheet
                                                                }
                                                            }
                                                            leaderboardRef.child(friend.username.toString())
                                                                .child("friends")
                                                                .setValue(listFriend2)
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("cancel", "loadPost:onCancelled")
                    }
                }
                val pointRef = leaderboardRef.child(currentUsername).child("pointsSheet")
                pointRef.addValueEventListener(pointsDataListener)
            }


            when (exerciseType) {
                56 -> {
                    ref.child("running").get().addOnSuccessListener {
                        if (it.exists()) {
                            val runningPoints = it.getValue<Int>()
                            if (runningPoints != null) {
                                ref.child("running").setValue(runningPoints + newPoints)
                                leaderboardRef.child(currentUsername).child("pointsSheet")
                                    .child("pointRunning").setValue(runningPoints + newPoints)
                            }
                        } else {
                            ref.child("running").setValue(newPoints)
                            leaderboardRef.child(currentUsername).child("pointsSheet")
                                .child("pointRunning").setValue(newPoints)
                        }

                        leaderboardRef.child(currentUsername).child("pointsSheet")
                            .child("totalPoints").get().addOnSuccessListener { points ->
                                val totalPoints = points.getValue<Int>()
                                if (totalPoints != null) {
                                    leaderboardRef.child(currentUsername).child("pointsSheet")
                                        .child("totalPoints").setValue(totalPoints + newPoints)
                                }
                            }
                        updateFriends()
                    }
                }

                79 -> {
                    ref.child("walking").get().addOnSuccessListener {
                        if (it.exists()) {
                            val walkingPoints = it.getValue<Int>()
                            if (walkingPoints != null) {
                                ref.child("walking").setValue(walkingPoints + newPoints)
                                leaderboardRef.child(currentUsername).child("pointsSheet")
                                    .child("pointsWalking").setValue(walkingPoints + newPoints)
                            }
                        } else {
                            ref.child("walking").setValue(newPoints)
                            leaderboardRef.child(currentUsername).child("pointsSheet")
                                .child("pointsWalking").setValue(newPoints)
                        }

                        leaderboardRef.child(currentUsername).child("pointsSheet")
                            .child("totalPoints").get().addOnSuccessListener { points ->
                                val totalPoints = points.getValue<Int>()
                                if (totalPoints != null) {
                                    leaderboardRef.child(currentUsername).child("pointsSheet")
                                        .child("totalPoints").setValue(totalPoints + newPoints)
                                }

                            }
                        updateFriends()
                    }
                }

                8 -> {
                    ref.child("cycling").get().addOnSuccessListener {
                        if (it.exists()) {
                            val cyclingPoints = it.getValue<Int>()
                            if (cyclingPoints != null) {
                                ref.child("cycling").setValue(cyclingPoints + newPoints)
                                leaderboardRef.child(currentUsername).child("pointsSheet")
                                    .child("pointsCycling").setValue(cyclingPoints + newPoints)
                            }
                        } else {
                            ref.child("cycling").setValue(newPoints)
                            leaderboardRef.child(currentUsername).child("pointsSheet")
                                .child("pointsCycling").setValue(newPoints)
                        }

                        leaderboardRef.child(currentUsername).child("pointsSheet")
                            .child("totalPoints").get().addOnSuccessListener { points ->
                                val totalPoints = points.getValue<Int>()
                                if (totalPoints != null) {
                                    leaderboardRef.child(currentUsername).child("pointsSheet")
                                        .child("totalPoints").setValue(totalPoints + newPoints)
                                }

                            }
                        updateFriends()
                    }
                }

                else -> {
                    ref.child("workout").get().addOnSuccessListener {
                        if (it.exists()) {
                            val workoutPoints = it.getValue<Int>()
                            if (workoutPoints != null) {
                                ref.child("workout").setValue(workoutPoints + newPoints)
                                leaderboardRef.child(currentUsername).child("pointsSheet")
                                    .child("pointsWorkout").setValue(workoutPoints + newPoints)
                            }
                        } else {
                            ref.child("workout").setValue(newPoints)
                            leaderboardRef.child(currentUsername).child("pointsSheet")
                                .child("pointsWorkout").setValue(newPoints)
                        }

                        leaderboardRef.child(currentUsername).child("pointsSheet")
                            .child("totalPoints").get().addOnSuccessListener { points ->
                                val totalPoints = points.getValue<Int>()
                                if (totalPoints != null) {
                                    leaderboardRef.child(currentUsername).child("pointsSheet")
                                        .child("totalPoints").setValue(totalPoints + newPoints)
                                }

                            }
                        updateFriends()
                    }
                }
            }

        }
}

