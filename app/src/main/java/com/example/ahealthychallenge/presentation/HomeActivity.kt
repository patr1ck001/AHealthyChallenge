package com.example.ahealthychallenge.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ahealthychallenge.data.HealthConnectManager
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class HomeActivity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume(){
        super.onResume()

        val activity = this
        val healthConnectManager = (application as BaseApplication).healthConnectManager


        setContent {
            HealthConnectApp(
                activity = activity,
                healthConnectManager = healthConnectManager,
                hasFriendRequest = false)
        }

        checkRequest(activity, healthConnectManager)

    }

    private fun checkRequest(activity: Activity, healthConnectManager: HealthConnectManager) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val dbref = Firebase.database.reference.child("Users")
        dbref.child(uid!!).get().addOnSuccessListener {
            if (it.exists()) {
                val username = it.value.toString()
                val dbref2 = FirebaseDatabase.getInstance().getReference("FriendRequests/$username")
                dbref2.get().addOnSuccessListener { user ->
                    if (user.exists()) {
                        for (userSnapshot in user.children) {
                            val requestType = userSnapshot.child("request_type").value.toString()
                            if (requestType == "received") {
                                setContent {
                                    HealthConnectApp(
                                        activity = activity,
                                        healthConnectManager = healthConnectManager,
                                        hasFriendRequest = true)
                                }
                                break
                            }
                        }

                    }
                }
            }
        }
    }


}