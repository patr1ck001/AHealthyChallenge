package com.example.ahealthychallenge.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.User
import com.example.ahealthychallenge.data.UserPointsSheet
import com.example.ahealthychallenge.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.IOException


class UserActivity: ComponentActivity(), ToastHelper  {

    //var for test only
    var toastHelper: ToastHelper? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var leaderboardRef: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var  fileInBytes: ByteArray
    private var flag: Boolean = false
    private lateinit var circleImageView: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard")
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        circleImageView = binding.profileImage
        circleImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 3)

        }

        binding.saveButton.setOnClickListener{

            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val username = binding.userName.text.toString()

            if(firstName.isEmpty() || lastName.isEmpty() || username.isEmpty()){
                showToast("Fill all fields")
            }
            else{
                databaseReference.child(username).get().addOnSuccessListener { it ->
                    if(it.exists()) {
                        showToast("Username already exists !")
                    }
                    else{
                        val user = User(firstName, lastName, username, uid)

                        if(uid != null){

                            databaseReference.child(uid).setValue(username)
                            databaseReference.child(username).setValue(user).addOnCompleteListener {

                                if(it.isSuccessful){
                                    leaderboardRef.child(username).child("pointsSheet").setValue(
                                        UserPointsSheet()
                                    )
                                    updateProfilePic(username)
                                    startActivity(Intent(this, HomeActivity::class.java))
                                }else{
                                    Toast.makeText(this, "failed to update profile !", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null){
            imageUri = data.data!!
            var bmp: Bitmap? = null
            try {
                bmp = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val baos = ByteArrayOutputStream()
            bmp!!.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            fileInBytes = baos.toByteArray()

            circleImageView.setImageURI(imageUri)
            flag = true
        }
    }

    private fun updateProfilePic(username: String) {
        storageReference = FirebaseStorage.getInstance().getReference("Users/$username")
        if(!flag) {
            imageUri = Uri.parse("android.resource://$packageName/${R.drawable.ic_profile_circle}")
            storageReference.putFile(imageUri).addOnCompleteListener {
                showToast("Profile successfully updated !")
            }.addOnFailureListener {
                Toast.makeText(this, "failed to upload the image !", Toast.LENGTH_LONG).show()
            }
        }
        else{
            storageReference.putBytes(fileInBytes).addOnCompleteListener {
               showToast("Profile successfully updated !")
            }.addOnFailureListener {
                Toast.makeText(this, "failed to upload the image !", Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun showToast(message: String) {
        //for test only
        toastHelper?.showToast(message)

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}