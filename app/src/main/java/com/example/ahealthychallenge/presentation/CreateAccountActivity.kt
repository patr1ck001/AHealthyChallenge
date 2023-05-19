package com.example.ahealthychallenge.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.databinding.ActivityCreateAccountBinding
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils.firebaseAuth
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils.firebaseUser

class CreateAccountActivity : ComponentActivity() {
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<EditText>

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.etEmail
        password = binding.etPassword
        confirmPassword = binding.etConfirmPassword

        createAccountInputsArray = arrayOf(email, password, confirmPassword)
        binding.btnCreateAccount.setOnClickListener {
            signIn()
        }


        binding.backButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            Toast.makeText(this, "please sign into your account", Toast.LENGTH_LONG).show()
            finish()
        }
    }


    private fun notEmpty(): Boolean = email.text.toString().trim().isNotEmpty() &&
            password.text.toString().trim().isNotEmpty() &&
            confirmPassword.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() &&
            password.text.toString().trim() == confirmPassword.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            createAccountInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {
            Toast.makeText(this, "passwords are not matching !", Toast.LENGTH_LONG).show()
        }
        return identical
    }

    private fun signIn() {
        if (identicalPassword()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userEmail = email.text.toString().trim()
            userPassword = password.text.toString().trim()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "created account successfully !", Toast.LENGTH_LONG).show()
                        sendEmailVerification()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "failed to Authenticate !", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "email sent to $userEmail",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}