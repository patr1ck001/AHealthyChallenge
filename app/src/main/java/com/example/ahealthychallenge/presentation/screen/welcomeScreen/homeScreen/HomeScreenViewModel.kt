package com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.data.serializables.LineDataSerializable
import com.example.ahealthychallenge.data.serializables.SerializableFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.himanshoe.charty.line.model.LineData


class HomeScreenViewModel(private val healthConnectManager: HealthConnectManager) :
    ViewModel() {
    private lateinit var database: DatabaseReference
    var lineData: MutableState<List<LineData>> = mutableStateOf(listOf())
    var homeScreenLoading: MutableState<Boolean> = mutableStateOf(true)
    var positionInLeaderboard: MutableState<Int> = mutableIntStateOf(1)
    var pointThisMonth: MutableState<Int> = mutableIntStateOf(0)
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    private val leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard")

    init {
        homeScreenLoading.value = true
        readLineData()
    }

    private fun readLineData() {
        database = Firebase.database.reference
        val refer = database.child("pointStats")
            .child(uid!!)
            .child("curveLine")
            .child("curveLineData")

        Log.d("curve", "refer $refer")

        val curveLineDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lineDataDb = snapshot.getValue<List<LineDataSerializable>>()
                Log.d("curve", "deserialized: $lineDataDb")
                if (lineDataDb != null) {
                    val lineDataList = lineDataDb.map { lineData ->
                        SerializableFactory.getLineData(lineData)
                    }
                    Log.d("curve", "deserialized2: $lineDataList")
                    lineData.value = lineDataList
                    pointThisMonth.value = lineDataList.map { point -> point.yValue.toInt() }.fold(0) { sum, element -> sum + element }

                    database.child("Users").child(uid!!).get().addOnSuccessListener { ds ->
                        if (ds.exists()) {
                            val currentUsername = ds.value.toString()
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
                                                        listFriend?.plus(currentUser)
                                                    positionInLeaderboard.value = leaderboardList?.sortedByDescending { friend -> friend.pointsSheet?.totalPoints }?.indexOf(Friend(
                                                        firstName = "(me)",
                                                        username = currentUsername,
                                                        pointsSheet = userPointsSheet
                                                    ))!! + 1
                                                    homeScreenLoading.value = false
                                                }
                                            }
                                    }
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("cancel", "loadPost:onCancelled")
            }
        }
        refer.addValueEventListener(curveLineDataListener)

        refer.get().addOnSuccessListener {
            val lineDataDb = it.getValue<List<LineDataSerializable>>()
            Log.d("curve", "deserialized: $lineDataDb")
            if (lineDataDb != null) {
                val lineDataList = lineDataDb.map { lineData ->
                    SerializableFactory.getLineData(lineData)
                }
                Log.d("curve", "deserialized2: $lineDataList")
                lineData.value = lineDataList
                pointThisMonth.value = lineDataList.map { point -> point.yValue.toInt() }.fold(0) { sum, element -> sum + element }



                database.child("Users").child(uid!!).get().addOnSuccessListener { ds ->
                    if (ds.exists()) {
                        val currentUsername = ds.value.toString()
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
                                                    listFriend?.plus(currentUser)
                                                positionInLeaderboard.value = leaderboardList?.sortedByDescending { friend -> friend.pointsSheet?.totalPoints }?.indexOf(Friend(
                                                    firstName = "(me)",
                                                    username = currentUsername,
                                                    pointsSheet = userPointsSheet
                                                ))!! + 1
                                                homeScreenLoading.value = false
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


class HomeScreenViewModelFactory(
    private val healthConnectManager: HealthConnectManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(
                healthConnectManager = healthConnectManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
