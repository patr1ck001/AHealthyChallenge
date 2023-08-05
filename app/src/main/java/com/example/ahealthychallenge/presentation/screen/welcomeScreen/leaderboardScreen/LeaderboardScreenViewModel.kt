package com.example.ahealthychallenge.presentation.screen.welcomeScreen.leaderboardScreen

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.UserPointsSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class LeaderboardScreenViewModel() : ViewModel() {
    val friends: MutableState<List<Friend>> = mutableStateOf(listOf())
    private val leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard")
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    val database = Firebase.database.reference
    private val dbRef = Firebase.database.reference.child("Users")
    private var storage = FirebaseStorage.getInstance().getReference("Users")
    var leaderboardLoading: MutableState<Boolean> = mutableStateOf(true)



    init {
        leaderboardLoading.value = true
        readFriends()
    }

    private fun readFriends() {
        val localFile = File.createTempFile("tempImage", "jpeg")

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
                                        val userPointsSheet =
                                            currentUserPointsSheet.getValue<UserPointsSheet>()
                                        val currentUser = listOf(
                                            Friend(
                                                firstName = "(me)",
                                                username = currentUsername,
                                                pointsSheet = userPointsSheet
                                            )
                                        )
                                        val leaderboardList =
                                            listFriend?.plus(currentUser)?.toMutableList()
                                        var i = 1
                                        if (leaderboardList != null) {
                                            for (friend in leaderboardList) {
                                                friend.username?.let { it1 ->
                                                    dbRef.child(it1).get()
                                                        .addOnSuccessListener { user ->
                                                            if (user.exists()) {
                                                                //lateinit var bitmap: Bitmap
                                                                storage.child(it1).getFile(localFile)
                                                                    .addOnSuccessListener {
                                                                        val bitmap =
                                                                            BitmapFactory.decodeFile(
                                                                                localFile.absolutePath
                                                                            )
                                                                        friend.bitmap = bitmap
                                                                        Log.d("leaderboardDBUG", "i: $i size: ${leaderboardList.size}")
                                                                        if(i == leaderboardList.size){
                                                                            // sort in descending order
                                                                            friends.value =
                                                                                leaderboardList.sortedByDescending { friend -> friend.pointsSheet?.totalPoints }
                                                                            leaderboardLoading.value = false
                                                                        }
                                                                        i++
                                                                    }
                                                            }
                                                        }
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                    }
            }
        }
        //friends.value = mockDataFriend
    }

}

class LeaderboardScreenViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaderboardScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaderboardScreenViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}