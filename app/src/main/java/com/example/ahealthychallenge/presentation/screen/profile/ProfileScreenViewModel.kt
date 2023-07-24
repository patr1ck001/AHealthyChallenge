package com.example.ahealthychallenge.presentation.screen.profile


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahealthychallenge.data.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
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
                //lateinit var bitmap: Bitmap
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
