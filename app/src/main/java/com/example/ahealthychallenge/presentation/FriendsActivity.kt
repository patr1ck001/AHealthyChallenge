package com.example.ahealthychallenge.presentation

import android.R.attr.data
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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



        binding.addFriendBtn.setOnClickListener {
            val intent = Intent(this, SearchUserActivity::class.java)
            this.startActivity(intent)


        }
    }

    fun clearList() {
        for(friend in friendArrayList){
            friendArrayList.remove(friend)
        }
    }

    override fun onResume() {
        super.onResume()

        clearList()
        friendRecyclerView.adapter = FriendAdapter(
            friendArrayList,
            username
        )

        val user = FirebaseAuth.getInstance().currentUser?.uid
        database.child(user!!).get().addOnSuccessListener {
            if(it.exists()){
                username = it.value.toString()
                getFriendData(username)
            }

            //Log.d("PROVA", "ciao " + friendArrayList[0].firstName!!)
            //Log.d("PROVA", "ciao " + friendArrayList[1].firstName!!)
        }
    }




    private fun getFriendData(username: String) {
        val localfile = File.createTempFile("tempImage", "jpeg")
        dbref = FirebaseDatabase.getInstance().getReference("FriendRequests/$username")
        dbref.get().addOnSuccessListener {


            // dbref.addValueEventListener(object : ValueEventListener{
            //  override fun onDataChange(snapshot: DataSnapshot) {
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
                                    friendArrayList.add(friend)
                                    friendRecyclerView.adapter = FriendAdapter(
                                        friendArrayList,
                                        username
                                    )
                                }
                        }
                    }
                }
            }
            // }

            /*  override fun onCancelled(error: DatabaseError) {
             //   TODO("Not yet implemented")
            }*/
            // })
        }
    }
}