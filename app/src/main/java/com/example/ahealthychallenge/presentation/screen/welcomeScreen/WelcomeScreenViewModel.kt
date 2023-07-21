package com.example.ahealthychallenge.presentation.screen.welcomeScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.data.serializables.LineDataSerializable
import com.example.ahealthychallenge.data.serializables.SerializableFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.himanshoe.charty.line.model.LineData

class WelcomeScreenViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    private lateinit var database: DatabaseReference
    var curveLineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        readCurveLineData()
    }

    private fun readCurveLineData() {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child(uid!!)
            .child("curveLine")
            .child("curveLineData")

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
}


class WelcomeScreenViewModelFactory(
    private val healthConnectManager: HealthConnectManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WelcomeScreenViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
