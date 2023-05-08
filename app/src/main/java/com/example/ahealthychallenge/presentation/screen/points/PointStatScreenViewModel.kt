package com.example.ahealthychallenge.presentation.screen.points

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModel

class PointStatScreenViewModel(private val healthConnectManager: HealthConnectManager): ViewModel() {

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
