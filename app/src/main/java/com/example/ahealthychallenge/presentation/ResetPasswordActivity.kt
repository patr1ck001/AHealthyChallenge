package com.example.ahealthychallenge.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.databinding.ActivityResetPasswordBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : ComponentActivity(), ToastHelper {

    //var for test only
    var toastHelper: ToastHelper? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var email: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        email = binding.emailText

        binding.btnReset.setOnClickListener{
            auth.sendPasswordResetEmail(email.text.toString())
                .addOnSuccessListener {
                    showToast("New password sent !")
            }
            .addOnFailureListener {
                Toast.makeText(this, "Unable to send new password !", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun showToast(message: String) {
        //for test only
        toastHelper?.showToast(message)

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}