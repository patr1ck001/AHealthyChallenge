package com.example.ahealthychallenge.presentation

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.data.serializables.LineDataSerializable
import com.example.ahealthychallenge.databinding.ActivitySearchUserBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class SearchUserActivity : ComponentActivity(), ToastHelper {

    //var for test only
    var toastHelper: ToastHelper? = null

    private lateinit var searchText: EditText
    private lateinit var searchBtn: ImageButton
    private lateinit var image: CircleImageView
    private lateinit var name: TextView
    private lateinit var requestBtn: Button
    private lateinit var refuseBtn: Button
    private lateinit var friendUsername: String
    var currentUsername: String = ""
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var leaderboardRef: DatabaseReference
    private lateinit var firebase: DatabaseReference
    private lateinit var progressBar: ProgressBar
    private var currentState = "not_friends"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        firebase = FirebaseDatabase.getInstance().getReference("Users")
        val storage = FirebaseStorage.getInstance().getReference("Users")
        firebaseRef = FirebaseDatabase.getInstance().getReference("FriendRequests")
        leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard")
        val localfile = File.createTempFile("tempImage", "jpeg")
        val user = FirebaseAuth.getInstance().currentUser?.uid

        progressBar = binding.progressBar2
        searchText = binding.searchText
        searchBtn = binding.searchBtn
        image = binding.image
        name = binding.nameSurname
        requestBtn = binding.requestBtn
        refuseBtn = binding.refuseBtn


        progressBar.visibility = View.GONE

        if(user != null){
            firebase.child(user).get().addOnSuccessListener { it ->
                if (it.exists()) {
                    currentUsername = it.value.toString()
                }
            }
        }


        refuseBtn.setOnClickListener{
            cancelRequest()
        }

        requestBtn.setOnClickListener{

            if(currentState == "not_friends"){
                sendRequestFriend()
            }
            else if(currentState == "request_sent"){
                cancelRequest()
            }
            else if(currentState == "friend"){
                showDialog()
            }
            else if(currentState == "received"){
                acceptRequest()
             }
        }

        searchBtn.setOnClickListener{
            name.visibility = View.GONE
            image.visibility = View.GONE
            requestBtn.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            val searchTerm = searchText.text.toString()
            if(searchTerm.isEmpty()){
                searchText.setError("Invalid Username")
                return@setOnClickListener
            }else{
                friendUsername = searchTerm
                firebase.child(searchTerm).get().addOnSuccessListener {
                    if(it.exists()){
                        searchText.text.clear()
                        name.text = it.child("firstName").value.toString() + " " + it.child("lastName").value.toString()
                        name.visibility = View.VISIBLE


                        storage.child(searchTerm).getFile(localfile).addOnSuccessListener {
                            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                            if(bitmap == null){
                                image.setImageResource(R.drawable.ic_profile_circle)
                            }else{
                                image.setImageBitmap(bitmap)
                            }
                        }
                        image.visibility = View.VISIBLE

                        if(searchTerm == currentUsername){
                            requestBtn.visibility = View.GONE
                        }else {
                            maintenanceOfButton()
                            requestBtn.visibility = View.VISIBLE
                        }

                        progressBar.visibility = View.GONE

                    }else{
                        showToast("username not founded !")
                    }
                }
            }
        }
    }



    private fun maintenanceOfButton() {
        firebaseRef.child(currentUsername).get().addOnSuccessListener {
            if(it.hasChild(friendUsername)){
                val requestType = it.child(friendUsername).child("request_type").value.toString()
                if(requestType == "sent") {
                    requestBtn.text = "Request Sent"
                    currentState = "request_sent"
                }
                else if (requestType == "friend"){
                    requestBtn.text = "Delete"
                    currentState = "friend"
                }
                else if (requestType == "received"){
                    requestBtn.text = "Accept"
                    refuseBtn.visibility = View.VISIBLE
                    currentState = "received"
                }
            }
            else{
                currentState = "not_friends"
                requestBtn.text = "Send Request"
            }
        }
    }


    private fun sendRequestFriend() {
        firebaseRef.child(currentUsername).child(friendUsername).child("request_type").setValue("sent").addOnSuccessListener {
            firebaseRef.child(friendUsername).child(currentUsername).child("request_type").setValue("received").addOnSuccessListener {
                requestBtn.text = "Request Sent"
                currentState = "request_sent"
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun cancelRequest() {
         var fnd = Friend(username = null)
        firebaseRef.child(currentUsername).child(friendUsername).removeValue().addOnSuccessListener {
            firebaseRef.child(friendUsername).child(currentUsername).removeValue().addOnSuccessListener {
                currentState = "not_friends"
                requestBtn.text = "Send Request"
                refuseBtn.visibility = View.GONE
            }
        }

        val ref = leaderboardRef.child(currentUsername).child("friends")
            ref.get().addOnSuccessListener {
                if (it.exists()) {
                    val list =  it.getValue<MutableList<Friend>>()
                    list?.map{ friend ->
                        if(friend.username == friendUsername){
                            fnd = friend
                        }
                    }

                    if(fnd.username != null) {
                        list?.remove(fnd)
                        ref.setValue(list)
                    }
                }
            }
        val refer = leaderboardRef.child(friendUsername).child("friends")
        refer.get().addOnSuccessListener {
            if (it.exists()) {
                val list =  it.getValue<MutableList<Friend>>()
                list?.map{ friend ->
                    if(friend.username == currentUsername){
                        fnd = friend
                    }
                }

                if(fnd.username != null) {
                    list?.remove(fnd)
                    refer.setValue(list)
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun acceptRequest() {
        firebaseRef.child(currentUsername).child(friendUsername).child("request_type").setValue("friend").addOnSuccessListener {
            firebaseRef.child(friendUsername).child(currentUsername).child("request_type").setValue("friend").addOnSuccessListener {
                requestBtn.text = "Delete"
                refuseBtn.visibility = View.GONE
                currentState = "friend"
            }
        }

        val ref = leaderboardRef.child(currentUsername).child("friends")
        firebase.child(friendUsername).get().addOnSuccessListener{ friend ->
            if(friend.exists()){
                leaderboardRef.child(friendUsername).child("pointsSheet").get().addOnSuccessListener { pnt ->
                    if (pnt.exists()) {
                        val points = pnt.getValue<UserPointsSheet>()
                        ref.get().addOnSuccessListener {
                            if(it.exists()) {
                                val list =  it.getValue<MutableList<Friend>>()
                                list?.add(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = friendUsername,
                                        pointsSheet = points
                                    )
                                )
                                ref.setValue(list)
                            }
                            else{
                                val list = mutableListOf(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = friendUsername,
                                        pointsSheet = points
                                    )
                                )
                                ref.setValue(list)
                            }
                        }
                    }
                }
            }
        }
        val refer = leaderboardRef.child(friendUsername).child("friends")
        firebase.child(currentUsername).get().addOnSuccessListener { friend ->
            if(friend.exists()){
                leaderboardRef.child(currentUsername).child("pointsSheet").get().addOnSuccessListener { pnt ->
                    if (pnt.exists()) {
                        val points = pnt.getValue<UserPointsSheet>()
                        refer.get().addOnSuccessListener {
                            if(it.exists()) {
                                val list =  it.getValue<MutableList<Friend>>()
                                list?.add(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = currentUsername,
                                        pointsSheet = points
                                    )
                                )
                                refer.setValue(list)
                            }
                            else{
                                val list = mutableListOf(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = currentUsername,
                                        pointsSheet = points
                                    )
                                )
                                refer.setValue(list)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDialog() {
       MaterialAlertDialogBuilder(this).setTitle("Alert").setMessage("Delete friend. Are you sure?")
           .setNegativeButton("No") {dialog, which -> }
           .setPositiveButton("Yes") {dialog, which -> cancelRequest()
               Toast.makeText(this, "Friend deleted !", Toast.LENGTH_LONG).show() } .show()
    }

    override fun showToast(message: String) {
        //for test only
        toastHelper?.showToast(message)

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}