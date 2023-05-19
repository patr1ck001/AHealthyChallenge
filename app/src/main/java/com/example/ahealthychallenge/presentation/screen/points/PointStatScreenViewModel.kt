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
    var pieData: MutableState<List<PieData>> = mutableStateOf(
        listOf(
            PieData(3F),
            PieData(1F),
            PieData(1F),
        )
    )
        private set

    var curveLineData: MutableState<List<LineData>> = mutableStateOf(
        listOf(
            LineData(1, 3F),
            LineData(2, 15F),
            LineData(3, 9F),
            LineData(4, 3F),
            LineData(5, 7F)
        )
    )
        private set
    var refreshing: MutableState<Boolean> = mutableStateOf(false)

    init {
        readPieChartData()
        readCurveLineData()
    }

    fun refreshing() {
        readPieChartData()
        readCurveLineData()
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
