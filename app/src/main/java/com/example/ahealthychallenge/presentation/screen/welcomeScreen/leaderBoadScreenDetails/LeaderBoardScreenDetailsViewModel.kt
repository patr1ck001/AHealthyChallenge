package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderBoadScreenDetails

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModel
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderboardScreen.LeaderboardScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class LeaderBoardScreenDetailsViewModel(
    private val username: String,
) : ViewModel() {
    val userPointsSheet: MutableState<UserPointsSheet> = mutableStateOf(UserPointsSheet())
    private val leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard")
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    val database = Firebase.database.reference
    var leaderboardDetailLoading: MutableState<Boolean> = mutableStateOf(true)

    init {
        leaderboardDetailLoading.value = true
        getUserPointsSheet()
    }

    private fun getUserPointsSheet() {
        database.child("Users").child(uid!!).get().addOnSuccessListener { it ->
            if (it.exists()) {
                val currentUsername = it.value.toString()
                leaderboardRef.child(currentUsername).child("friends").get()
                    .addOnSuccessListener { list ->
                        if (list.exists()) {
                            val listFriend = list.getValue<List<Friend>>()

                            leaderboardRef.child(currentUsername).child("pointsSheet").get()
                                .addOnSuccessListener { currentUserPointsSheet ->
                                    if (currentUserPointsSheet.exists()) {
                                        val userPoints = currentUserPointsSheet.getValue<UserPointsSheet>()
                                        val currentUser = listOf(
                                            Friend(
                                                username = currentUsername,
                                                pointsSheet = userPoints
                                            )
                                        )
                                        val leaderboardList = listFriend?.plus(currentUser)
                                        val currentFriend =
                                            leaderboardList?.filter { friend -> friend.username == username }
                                        userPointsSheet.value = currentFriend?.get(0)?.pointsSheet!!
                                        leaderboardDetailLoading.value = false
                                    }
                                }
                        }
                    }
            }
        }
    }
}


class LeaderBoardScreenDetailsViewModelFactory(
    private val username: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderBoardScreenDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderBoardScreenDetailsViewModel(
                username = username
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}