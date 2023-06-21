package com.example.ahealthychallenge.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.databinding.ActivityResetPasswordBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : ComponentActivity() {

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
                    Toast.makeText(this, "Password sent !", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Unable to send new password !", Toast.LENGTH_LONG).show()
            }

        }
    }
}