package com.example.ahealthychallenge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.adaptive.calculateDisplayFeatures

class HomeActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        val healthConnectManager = (application as BaseApplication).healthConnectManager
        setContent {
             HealthConnectApp(
                 activity = activity,
                 healthConnectManager = healthConnectManager)
         }
    }
}