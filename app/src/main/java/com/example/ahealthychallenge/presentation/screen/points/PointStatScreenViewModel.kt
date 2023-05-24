package com.example.ahealthychallenge.presentation.screen.points

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.data.serializables.LineDataSerializable
import com.example.ahealthychallenge.data.serializables.SerializableFactory
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.himanshoe.charty.line.model.LineData
import com.himanshoe.charty.pie.config.PieData
import java.time.Instant

class PointStatScreenViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    private lateinit var database: DatabaseReference
    var pieData: MutableState<List<PieData>> = mutableStateOf(listOf())
        private set

    var curveLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var walkingLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var runningLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var bikingLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var workoutLineData: MutableState<List<LineData>> = mutableStateOf(listOf())

    var refreshing: MutableState<Boolean> = mutableStateOf(false)

    init {
        readPieChartData()
        readCurveLineData()
        readLineData("walkingLineData")
        readLineData("runningLineData")
        readLineData("bikingLineData")
        readLineData("workoutLineData")
    }

    fun refreshing() {
        readPieChartData()
        readCurveLineData()
        readLineData("walkingLineData")
        readLineData("runningLineData")
        readLineData("bikingLineData")
        readLineData("workoutLineData")
    }

    fun readPieChartData() {
        val database = Firebase.database.reference
        var walkingPoints: Int
        var runningPoints: Int
        var cyclingPoints: Int
        var workoutPoints: Int

        val ref = database.child("pointStats")
            .child("userID")
            .child("pieChart")
            .child("pieChartData")
        Log.d("map", "before refer")
        ref.get().addOnSuccessListener {
            //we check is the list of session on the database is up to date
            val dbMap = it.getValue<Map<String, Int>>()
            if (dbMap != null) {
                Log.d("map", "the map is $dbMap")
                walkingPoints = dbMap["walking"]!!
                runningPoints = dbMap["running"]!!
                cyclingPoints = dbMap["cycling"]!!
                workoutPoints = dbMap["workout"]!!
                pieData.value = listOf(
                    PieData(walkingPoints.toFloat()),
                    PieData(runningPoints.toFloat()),
                    PieData(cyclingPoints.toFloat()),
                    PieData(workoutPoints.toFloat())

                )
            }
        }
        refreshing.value = false
    }

    fun readCurveLineData() {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child("userID")
            .child("curveLine")
            .child("curveLineData")

        Log.d("curve", "refer $refer")
        refer.get().addOnSuccessListener {
            val curveLineDataDb = it.getValue<List<LineDataSerializable>>()
            Log.d("curve", "deserialized: $curveLineDataDb")
            if (curveLineDataDb != null) {
                val curveLineList = curveLineDataDb.map { lineData ->
                    SerializableFactory.getLineData(lineData)
                }
                Log.d("curve", "deserialized2: $curveLineList")
                curveLineData.value = curveLineList
            }
        }

        Log.d("curve", "before")
    }

    fun readLineData(pathName: String) {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child("userID")
            .child(pathName)

        Log.d("viewLine", "refer $refer")
        refer.get().addOnSuccessListener {
            val lineDataDb = it.getValue<List<LineDataSerializable>>()
            Log.d("viewLine", "deserialized: $lineDataDb")
            if (lineDataDb != null) {
                val lineDataList = lineDataDb.map { lineData ->
                    SerializableFactory.getLineData(lineData)
                }
                Log.d("curve", "deserialized2: $lineDataList")

                when(pathName){
                    "walkingLineData" -> walkingLineData.value = lineDataList
                    "runningLineData" -> runningLineData.value = lineDataList
                    "bikingLineData" -> bikingLineData.value = lineDataList
                    "workoutLineData" -> workoutLineData.value = lineDataList
                }
            }
        }
        Log.d("viewLine", "before")
    }
    /*fun readCurveLineData() {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child("userID")
            .child("curveLine")
            .child("curveLineData")


        refer.get().addOnCompleteListener {
            Log.d("curve", "enter in reference")
            if (it.isSuccessful) {
                Log.d("curve", "it is successful")

                val gti = object :
                    GenericTypeIndicator<MutableList<LineDataSerializable>>() {}
                val curveLineDataDb = it.result.getValue(gti)
                if (curveLineDataDb != null) {
                    /*val curveLineList = curveLineDataDb.map { lineData ->
                                    SerializableFactory.getLineData(lineData)
                                }
                                Log.d("curve", "deserialized2: $curveLineList")
                                curveLineData.value = curveLineList*/
                    Log.d("curve", "Jesus is my Lord!")
                }
            }
        }
    }*/
}

class PointStatScreenViewModelFactory(
    private val healthConnectManager: HealthConnectManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PointStatScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PointStatScreenViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
