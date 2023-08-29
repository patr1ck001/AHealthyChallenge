package com.example.ahealthychallenge.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.databinding.ActivityCreateAccountBinding
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils.firebaseAuth
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : ComponentActivity(), ToastHelper {

    //var for test only
    var toastHelper: ToastHelper? = null

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
           showToast("passwords are not matching !")
        }
        return identical
    }

    private fun pswLength(): Boolean{
        var length = false
        if(password.text!!.length > 5 ){
            length = true
        }
        else if (password.text!!.length  <=5  && identicalPassword()){
            showToast("password is too short !")
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
                        sendEmailVerification()
                    } else {
                        showToast("failed to Authenticate !")
                    }
                }
        }
    }

    /* send verification email to the new user. This will only
    *  work if the firebase user is not null.
    */

    private fun sendEmailVerification() {

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        //send email verification
        firebaseUser!!.sendEmailVerification()
            .addOnSuccessListener {
               showToast("Check your email for verification !")
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to send verification due to " + e.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun showToast(message: String) {
        //for test only
        toastHelper?.showToast(message)

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}