package com.example.ahealthychallenge.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.Friend
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import de.hdodenhof.circleimageview.CircleImageView

class FriendAdapter2(private val friendList: ArrayList<Friend>, private val context: Context): RecyclerView.Adapter<FriendAdapter2.MyViewHolder>(){

    private var firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("FriendRequests")
    private var  leaderboardRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("leaderboard")
    private var  firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendAdapter2.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_item_2, parent, false)
        return FriendAdapter2.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FriendAdapter2.MyViewHolder, position: Int) {
        val currentitem = friendList[position]
        holder.name.text = currentitem.firstName+" "+currentitem.lastName
        holder.friendUsername.text = currentitem.username
        if(currentitem.bitmap == null){
            holder.image.setImageResource(R.drawable.ic_profile_circle)
        }
        else{
            holder.image.setImageBitmap(currentitem.bitmap)
        }


        holder.deleteButton.setOnClickListener{
            MaterialAlertDialogBuilder(context).setTitle("Alert").setMessage("Delete friend. Are you sure?")
                .setNegativeButton("No") {dialog, which -> }
                .setPositiveButton("Yes") {dialog, which -> deleteFriend(currentitem, holder)
                    Toast.makeText(context, "Friend deleted !", Toast.LENGTH_LONG).show() } .show()
        }

    }

    private fun deleteFriend(currentitem: Friend, holder: FriendAdapter2.MyViewHolder) {
        firebaseRef.child(currentitem.currentUsername!!).child(holder.friendUsername.text.toString()).removeValue().addOnSuccessListener {
            firebaseRef.child(holder.friendUsername.text.toString()).child(currentitem.currentUsername!!).removeValue().addOnSuccessListener {
                holder.name.visibility = View.GONE
                holder.image.visibility = View.GONE
                holder.friendUsername.visibility = View.GONE
                holder.deleteButton.visibility = View.GONE
            }
        }

        lateinit var fnd: Friend
        val ref = leaderboardRef.child(currentitem.currentUsername!!).child("friends")
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                val list =  it.getValue<MutableList<Friend>>()
                list?.map{ friend ->
                    if(friend.username == holder.friendUsername.text.toString()){
                        fnd = friend
                    }
                }

                list?.remove(fnd)
                ref.setValue(list)

            }
        }
        val refer = leaderboardRef.child(holder.friendUsername.text.toString()).child("friends")
        refer.get().addOnSuccessListener {
            if (it.exists()) {
                val list =  it.getValue<MutableList<Friend>>()
                list?.map{ friend ->
                    if(friend.username == currentitem.currentUsername!!){
                        fnd = friend
                    }
                }

                list?.remove(fnd)
                refer.setValue(list)

            }
        }
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.nam_surnam2)
        var image: CircleImageView = itemView.findViewById(R.id.img2)
        val friendUsername: TextView = itemView.findViewById(R.id.username2)
        val deleteButton: Button = itemView.findViewById(R.id.delete_btn)
    }


}