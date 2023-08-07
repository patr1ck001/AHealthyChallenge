package com.example.ahealthychallenge.presentation.screen.profile


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.Friend
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
import com.google.firebase.storage.FirebaseStorage
import java.io.File
// TODO: when the user does not update the image there is nothing at
class ProfileScreenViewModel() : ViewModel() {
    private var dbref: DatabaseReference
    private lateinit var username: String
    var profileLoading: MutableState<Boolean> = mutableStateOf(true)
    var currentUser: MutableState<Friend> = mutableStateOf(Friend(null, null, null, null, null))
    private var storage = FirebaseStorage.getInstance().getReference("Users")
    var positionInLeaderboard: MutableState<Int> = mutableIntStateOf(1)
    var pointThisMonth: MutableState<Int> = mutableIntStateOf(0)
    var totalFriends: MutableState<Int> = mutableIntStateOf(0)
    var uid = FirebaseAuth.getInstance().currentUser?.uid
    val database = Firebase.database.reference
    val refer = database.child("pointStats")
        .child(uid!!)
        .child("curveLine")
        .child("curveLineData")
    private val leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard")

    init {
        profileLoading.value = true
        val user = FirebaseAuth.getInstance().currentUser?.uid
        dbref = Firebase.database.reference.child("Users")
        dbref.child(user!!).get().addOnSuccessListener {
            if (it.exists()) {
                username = it.value.toString()
                getUserData(username)
            }
        }
    }

    private fun getUserData(username: String) {
        val localFile = File.createTempFile("tempImage", "jpeg")


        dbref.child(username).get().addOnSuccessListener { user ->
            if (user.exists()) {
                storage.child(username).getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    val myUser = Friend(
                        firstName = user.child("firstName").value.toString(),
                        lastName = user.child("lastName").value.toString(),
                        username = user.child("username").value.toString(),
                        bitmap = bitmap
                    )
                    currentUser.value = myUser
                    profileLoading.value = false
                }
            }
        }

        val curveLineDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lineDataDb = snapshot.getValue<List<LineDataSerializable>>()
                if (lineDataDb != null) {
                    val lineDataList = lineDataDb.map { lineData ->
                        SerializableFactory.getLineData(lineData)
                    }
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
                                                    totalFriends.value = listFriend.size
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

    }
}


class ProfileScreenViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileScreenViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
