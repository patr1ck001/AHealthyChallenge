package com.example.ahealthychallenge.presentation

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.data.User
import com.example.ahealthychallenge.databinding.ActivityFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.values
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FriendsActivity : ComponentActivity() {

    private lateinit var dbref: DatabaseReference
    private  var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private var storage = FirebaseStorage.getInstance().getReference("Users")
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var friendArrayList: ArrayList<Friend>
    private var username: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendRecyclerView = binding.friendList
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.setHasFixedSize(true)


        friendArrayList = arrayListOf()

        val user = FirebaseAuth.getInstance().currentUser?.uid
        database.child(user!!).get().addOnSuccessListener {
            if(it.exists()){
                username = it.value.toString()
                getFriendData(username)
            }

            //Log.d("PROVA", "ciao " + friendArrayList[0].firstName!!)
            //Log.d("PROVA", "ciao " + friendArrayList[1].firstName!!)
        }

        binding.addFriendBtn.setOnClickListener {
            val intent = Intent(this, SearchUserActivity::class.java)
            this.startActivity(intent)


        }
    }

    private fun getFriendData(username: String) {
        val localfile = File.createTempFile("tempImage", "jpeg")
        dbref = FirebaseDatabase.getInstance().getReference("FriendRequests/$username")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        var friend = Friend(null, null, null)
                        val requestType = userSnapshot.child("request_type").value.toString()
                        if(requestType == "received"){
                           database.child(userSnapshot.key.toString()).get().addOnSuccessListener {
                               val firstname = it.child("firstName").value.toString()
                               val lastname = it.child("lastName").value.toString()
                               storage.child(userSnapshot.key.toString()).getFile(localfile).addOnSuccessListener {
                                   val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                                   friend.bitmap = bitmap
                                   friend.firstName = firstname
                                   friend.lastName = lastname
                                   friendArrayList.add(friend)
                                   friendRecyclerView.adapter = FriendAdapter(friendArrayList, username, userSnapshot.key.toString())
                               }
                           }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}