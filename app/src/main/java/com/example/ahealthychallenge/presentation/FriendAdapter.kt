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
import de.hdodenhof.circleimageview.CircleImageView


class FriendAdapter(private val friendList: ArrayList<Friend>): RecyclerView.Adapter<FriendAdapter.MyViewHolder>(){

    private var firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("FriendRequests")

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
            holder.image.setImageResource(R.drawable.ic_profile)
        }
        else{
            holder.image.setImageBitmap(currentitem.bitmap)
        }

        holder.acceptButton.setOnClickListener {
            firebaseRef.child(currentitem.currentUsername!!).child(holder.friendUsername.text.toString()).child("request_type").setValue("friend").addOnSuccessListener {
                firebaseRef.child(holder.friendUsername.text.toString()).child(currentitem.currentUsername!!).child("request_type").setValue("friend").addOnSuccessListener {
                    holder.refuseButton.visibility = View.GONE
                    holder.acceptButton.text = "Friend"
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