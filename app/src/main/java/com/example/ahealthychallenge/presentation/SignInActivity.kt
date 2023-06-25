package com.example.ahealthychallenge.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.databinding.ActivitySignInBinding
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils.firebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignInActivity:  ComponentActivity() {


    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<TextInputEditText>

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var button: SignInButton

    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        button = binding.googleButton

        configureGoogleSignIn()

        setupUI()

        email = binding.emailEditText
        password = binding.passwordEditText

        signInInputsArray = arrayOf(email, password)
        binding.btnCreateAccount2.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }

        binding.btnForget.setOnClickListener{
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signInUser() {
        signInEmail = email.text.toString().trim()
        signInPassword = password.text.toString().trim()

        if (notEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            databaseReference.child(user.uid).get().addOnSuccessListener { it ->
                                if(it.exists() && user.isEmailVerified){
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    Toast.makeText(this, "signed in successfully", Toast.LENGTH_LONG).show()
                                    finish()
                                }
                                else if(!(user.isEmailVerified)){
                                    Toast.makeText(this, "User isn't verified. Check your email !", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    val intent = Intent(this, UserActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "sign in failed", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            signInInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }

    private fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()

    /* check if there's a signed-in user*/
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        if (user != null) {
            databaseReference.child(user.uid).get().addOnSuccessListener { it ->
                if (it.exists()) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "welcome back", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
        button.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    databaseReference.child(user.uid).get().addOnSuccessListener { it ->
                        if(it.exists()){
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            val intent = Intent(this, UserActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

}

