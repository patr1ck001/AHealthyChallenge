package com.example.ahealthychallenge.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.databinding.ActivityCreateAccountBinding
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils.firebaseAuth
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils.firebaseUser
import com.google.android.material.textfield.TextInputEditText

class CreateAccountActivity : ComponentActivity() {
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<TextInputEditText>

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.emailEditText2
        password = binding.passwordEditText2
        confirmPassword = binding.confirmPasswordEditText

        createAccountInputsArray = arrayOf(email, password, confirmPassword)
        binding.btnCreateAccount.setOnClickListener {
            signIn()
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

    private fun pswLength(): Boolean{
        var length = false
        if(password.text!!.length > 5 ){
            length = true
        }
        else if (password.text!!.length  <=5  && identicalPassword()){
            Toast.makeText(this, "password is too short !", Toast.LENGTH_LONG).show()
        }

        return length
    }

    private fun signIn() {
        if (identicalPassword() && pswLength()) {
            // identicalPassword() returns true only  when inputs are not empty and passwords are identical
            userEmail = email.text.toString().trim()
            userPassword = password.text.toString().trim()

            /*create a user*/
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Success !", Toast.LENGTH_LONG).show()
                        sendEmailVerification()
                        startActivity(Intent(this, UserActivity::class.java))
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