package com.example.ahealthychallenge.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.Friend
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import de.hdodenhof.circleimageview.CircleImageView


class FriendAdapter(private val friendList: ArrayList<Friend>): RecyclerView.Adapter<FriendAdapter.MyViewHolder>(){

    private var firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("FriendRequests")
    private var  leaderboardRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("leaderboard")
    private var  firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    override fun getItemCount(): Int {
         return friendList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = friendList[position]
        holder.name.text = currentitem.firstName+" "+currentitem.lastName
        holder.friendUsername.text = currentitem.username
        if(currentitem.bitmap == null){
            holder.image.setImageResource(R.drawable.ic_profile_circle)
        }
        else{
            holder.image.setImageBitmap(currentitem.bitmap)
        }

        holder.acceptButton.setOnClickListener {
            var flag = false
            if(holder.acceptButton.text == "Friend"){
                flag = true
            }
            firebaseRef.child(currentitem.currentUsername!!).child(holder.friendUsername.text.toString()).child("request_type").setValue("friend").addOnSuccessListener {
                firebaseRef.child(holder.friendUsername.text.toString()).child(currentitem.currentUsername!!).child("request_type").setValue("friend").addOnSuccessListener {
                    holder.refuseButton.visibility = View.GONE
                    holder.acceptButton.text = "Friend"
                }
            }

            if(!flag){
                val ref = leaderboardRef.child(currentitem.currentUsername!!).child("friends")
                firebase.child(holder.friendUsername.text.toString()).get().addOnSuccessListener{ friend ->
                    if(friend.exists()){
                        ref.get().addOnSuccessListener {
                            if(it.exists()) {
                                val list =  it.getValue<MutableList<Friend>>()
                                list?.add(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = holder.friendUsername.text.toString()
                                    )
                                )
                                ref.setValue(list)
                            }
                            else{
                                val list = mutableListOf(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = holder.friendUsername.text.toString()
                                    )
                                )
                                ref.setValue(list)
                            }
                        }
                    }
                }
                val refer = leaderboardRef.child(holder.friendUsername.text.toString()).child("friends")
                firebase.child(currentitem.currentUsername!!).get().addOnSuccessListener { friend ->
                    if(friend.exists()){
                        refer.get().addOnSuccessListener {
                            if(it.exists()) {
                                val list =  it.getValue<MutableList<Friend>>()
                                list?.add(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = currentitem.currentUsername!!
                                    )
                                )
                                refer.setValue(list)
                            }
                            else{
                                val list = mutableListOf(
                                    Friend(
                                        firstName = friend.child("firstName").value.toString(),
                                        lastName = friend.child("lastName").value.toString(),
                                        username = currentitem.currentUsername!!
                                    )
                                )
                                refer.setValue(list)
                            }
                        }
                    }
                }
            }

        }

        holder.refuseButton.setOnClickListener {
            firebaseRef.child(currentitem.currentUsername!!).child(holder.friendUsername.text.toString()).removeValue().addOnSuccessListener {
                firebaseRef.child(holder.friendUsername.text.toString()).child(currentitem.currentUsername!!).removeValue().addOnSuccessListener {
                        holder.name.visibility = View.GONE
                        holder.image.visibility = View.GONE
                        holder.refuseButton.visibility = View.GONE
                        holder.acceptButton.visibility = View.GONE
                }
            }
        }


    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.nam_surnam)
        var image: CircleImageView = itemView.findViewById(R.id.img)
        val acceptButton: Button = itemView.findViewById(R.id.accept_btn)
        val refuseButton: Button = itemView.findViewById(R.id.refuse_btn)
        val friendUsername: TextView = itemView.findViewById(R.id.username)
    }


}