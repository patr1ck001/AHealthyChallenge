package com.example.ahealthychallenge.presentation.screen.points

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.data.serializables.LineDataSerializable
import com.example.ahealthychallenge.data.serializables.SerializableFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.himanshoe.charty.line.model.LineData
import com.himanshoe.charty.pie.config.PieData

class PointStatScreenViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    private lateinit var database: DatabaseReference
    var pieData: MutableState<List<PieData>> = mutableStateOf(listOf())
        private set
    var pieDataMap: MutableState<Map<String, Int>> = mutableStateOf(mapOf("walking" to 0))
        private set

    var pointStatScreenLoading: MutableState<Boolean> = mutableStateOf(true)

    var curveLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var walkingLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var runningLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var bikingLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var workoutLineData: MutableState<List<LineData>> = mutableStateOf(listOf())

    var uid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        pointStatScreenLoading.value = true
        readPieChartData()
        readCurveLineData()
        readLineData("walkingLineData")
        readLineData("runningLineData")
        readLineData("bikingLineData")
        readLineData("workoutLineData")
    }


    private fun readPieChartData() {
        val database = Firebase.database.reference
        var walkingPoints = 0
        var runningPoints = 0
        var cyclingPoints = 0
        var workoutPoints = 0

        val ref = database.child("pointStats")
            .child(uid!!)
            .child("pieChart")
            .child("pieChartData")
        ref.get().addOnSuccessListener {
            //we check is the list of session on the database is up to date
            val dbMap = it.getValue<Map<String, Int>>()
            if (dbMap != null) {
                if (dbMap["walking"] != null) {
                    walkingPoints = dbMap["walking"]!!
                }

                if (dbMap["running"] != null) {
                    runningPoints = dbMap["running"]!!

                }

                if (dbMap["cycling"] != null) {
                    cyclingPoints = dbMap["cycling"]!!
                }

                if (dbMap["workout"] != null) {
                    workoutPoints = dbMap["workout"]!!
                }

                pieData.value = listOf(
                    PieData(walkingPoints.toFloat()),
                    PieData(runningPoints.toFloat()),
                    PieData(cyclingPoints.toFloat()),
                    PieData(workoutPoints.toFloat())

                )
                pieDataMap.value = dbMap
            }
        }
    }

    private fun readCurveLineData() {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child(uid!!)
            .child("curveLine")
            .child("curveLineData")

        val curveLineDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val curveLineDataDb = snapshot.getValue<List<LineDataSerializable>>()
                if (curveLineDataDb != null) {
                    val curveLineList = curveLineDataDb.map { lineData ->
                        SerializableFactory.getLineData(lineData)
                    }
                    curveLineData.value = curveLineList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("cancel", "loadPost:onCancelled")
            }

        }
        refer.addValueEventListener(curveLineDataListener)

        refer.get().addOnSuccessListener {
            val curveLineDataDb = it.getValue<List<LineDataSerializable>>()
            if (curveLineDataDb != null) {
                val curveLineList = curveLineDataDb.map { lineData ->
                    SerializableFactory.getLineData(lineData)
                }
                curveLineData.value = curveLineList
            }
        }

    }

    private fun readLineData(pathName: String) {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child(uid!!)
            .child(pathName)


        Log.d("pointStats", "refer $refer")
        Log.d("pointStats", "refer $uid")
        val lineDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("pointStats", "enter")
                val lineDataDb = snapshot.getValue<List<LineDataSerializable>>()
                Log.d("pointStats", "deserialized: $lineDataDb")
                if (lineDataDb != null) {
                    val lineDataList = lineDataDb.map { lineData ->
                        SerializableFactory.getLineData(lineData)
                    }
                    Log.d("pointStats", "deserialized2: $lineDataList")

                    when (pathName) {
                        "walkingLineData" -> walkingLineData.value = lineDataList
                        "runningLineData" -> runningLineData.value = lineDataList
                        "bikingLineData" -> bikingLineData.value = lineDataList
                        "workoutLineData" -> workoutLineData.value = lineDataList
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("pointStats", "loadPost:onCancelled")
            }

        }
        refer.addValueEventListener(lineDataListener)
        refer.get().addOnSuccessListener {
            Log.d("pointStats", "enter")
            val lineDataDb = it.getValue<List<LineDataSerializable>>()
            Log.d("pointStats", "deserialized: $lineDataDb")
            if (lineDataDb != null) {
                val lineDataList = lineDataDb.map { lineData ->
                    SerializableFactory.getLineData(lineData)
                }
                Log.d("pointStats", "deserialized2: $lineDataList")

            }
            pointStatScreenLoading.value = false
        }
    }
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
