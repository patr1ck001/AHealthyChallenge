package com.example.ahealthychallenge.presentation.screen.points

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahealthychallenge.data.ExerciseSession
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.himanshoe.charty.pie.config.PieData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PointStatScreenViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    var pieData: MutableState<List<PieData>> = mutableStateOf(
        listOf(
            PieData(3F),
            PieData(1F),
            PieData(1F),
        )
    )
        private set

    var refreshing: MutableState<Boolean> = mutableStateOf(false)

    init {
        readPieChartData()
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
