package com.example.ahealthychallenge.presentation

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.databinding.ActivitySearchUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class SearchUserActivity : ComponentActivity() {

    private lateinit var searchText: EditText
    private lateinit var searchBtn: ImageButton
    private lateinit var image: CircleImageView
    private lateinit var name: TextView
    private lateinit var requestBtn: Button
    private lateinit var currentUsername: String
    private lateinit var friendUsername: String
    private lateinit var firebaseRef: DatabaseReference
    private var currentState = "not_friends"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firebase = FirebaseDatabase.getInstance().getReference("Users")
        val storage = FirebaseStorage.getInstance().getReference("Users")
        firebaseRef = FirebaseDatabase.getInstance().getReference("FriendRequests")
        val localfile = File.createTempFile("tempImage", "jpg")
        val user = FirebaseAuth.getInstance().currentUser?.uid

        searchText = binding.searchText
        searchBtn = binding.searchBtn
        image = binding.image
        name = binding.nameSurname
        requestBtn = binding.requestBtn

        searchText.requestFocus()

        firebase.child(user!!).get().addOnSuccessListener { it ->
            if (it.exists()) {
                currentUsername = it.value.toString()
            }
        }

        requestBtn.setOnClickListener{

           //currentState = checkRequestType()
            if(currentState == "not_friends"){
                sendRequestFriend()
            }
        }

        searchBtn.setOnClickListener{
            name.visibility = View.GONE
            image.visibility = View.GONE
            requestBtn.visibility = View.GONE

            currentState = "not_friends"

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
                                image.setImageResource(R.drawable.ic_profile)
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

                    }else{
                        Toast.makeText(this, "username not founded !", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

   /* private fun checkRequestType(): String {
        var requestType = "not_friends"
        firebaseRef.child(friendUsername).get().addOnSuccessListener {
            if (it.hasChild(currentUsername)) {
                requestType = it.child(currentUsername).child("request_type").value.toString()
            }
        }
        return requestType
    }  */

    private fun maintenanceOfButton() {
        firebaseRef.child(currentUsername).get().addOnSuccessListener {
            if(it.hasChild(friendUsername)){
                val requestType = it.child(friendUsername).child("request_type").value.toString()
                if(requestType == "sent") {
                    requestBtn.text = "Request Sent"
                    currentState = "request_sent"
                }
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
}