package com.example.ahealthychallenge.presentation

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ahealthychallenge.data.Friend
import com.example.ahealthychallenge.databinding.ActivityFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class FriendsActivity : ComponentActivity() {

    private lateinit var dbref: DatabaseReference
    private  var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private var storage = FirebaseStorage.getInstance().getReference("Users")
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var friendRecyclerView2: RecyclerView
    private lateinit var friendArrayList: ArrayList<Friend>
    private lateinit var friendArrayList2: ArrayList<Friend>
    private var username: String = ""
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefreshLayout = binding.refresh

        friendRecyclerView = binding.friendList
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.setHasFixedSize(true)

        friendRecyclerView2 = binding.friendList2
        friendRecyclerView2.layoutManager = LinearLayoutManager(this)
        friendRecyclerView2.setHasFixedSize(true)

        friendArrayList = arrayListOf()
        friendArrayList2 = arrayListOf()

        friendRecyclerView.adapter = FriendAdapter(
            friendArrayList
        )

        friendRecyclerView2.adapter = FriendAdapter2(
            friendArrayList2,
            this
        )


        swipeRefreshLayout.setOnRefreshListener {
            refreshList()
            swipeRefreshLayout.isRefreshing = false
        }

        binding.addFriendBtn.setOnClickListener {
            val intent = Intent(this, SearchUserActivity::class.java)
            this.startActivity(intent)


        }
    }



    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList(){


        friendArrayList.clear()
        friendArrayList2.clear()
        friendRecyclerView.adapter?.notifyDataSetChanged()
        friendRecyclerView2.adapter?.notifyDataSetChanged()

        val user = FirebaseAuth.getInstance().currentUser?.uid
        database.child(user!!).get().addOnSuccessListener {
            if(it.exists()){
                username = it.value.toString()
                getFriendData(username)

            }
        }


    }



    private fun getFriendData(username: String) {
        val localfile = File.createTempFile("tempImage", "jpeg")
        dbref = FirebaseDatabase.getInstance().getReference("FriendRequests/$username")
        dbref.get().addOnSuccessListener {
            if (it.exists()) {
                for (userSnapshot in it.children) {
                    var friend = Friend(null, null, null, null)
                    val requestType = userSnapshot.child("request_type").value.toString()
                    if (requestType == "received") {
                        database.child(userSnapshot.key.toString()).get().addOnSuccessListener {
                            val firstname = it.child("firstName").value.toString()
                            val lastname = it.child("lastName").value.toString()
                            storage.child(userSnapshot.key.toString()).getFile(localfile)
                                .addOnSuccessListener {
                                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                                    friend.bitmap = bitmap
                                    friend.firstName = firstname
                                    friend.lastName = lastname
                                    friend.username = userSnapshot.key.toString()
                                    friend.currentUsername = username
                                    friendArrayList.add(friend)
                                    friendRecyclerView.adapter = FriendAdapter(
                                        friendArrayList
                                    )
                                }
                        }
                    }
                    else if(requestType == "friend"){
                        database.child(userSnapshot.key.toString()).get().addOnSuccessListener {
                            val firstname = it.child("firstName").value.toString()
                            val lastname = it.child("lastName").value.toString()
                            storage.child(userSnapshot.key.toString()).getFile(localfile)
                                .addOnSuccessListener {
                                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                                    friend.bitmap = bitmap
                                    friend.firstName = firstname
                                    friend.lastName = lastname
                                    friend.username = userSnapshot.key.toString()
                                    friend.currentUsername = username
                                    friendArrayList2.add(friend)
                                    friendRecyclerView2.adapter = FriendAdapter2(
                                        friendArrayList2,
                                        this
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}