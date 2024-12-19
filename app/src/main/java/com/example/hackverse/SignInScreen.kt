package com.example.hackverse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import android.annotation.SuppressLint
import com.google.android.material.textfield.TextInputEditText

class SignInScreen : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    lateinit var databaseReference: DatabaseReference

    companion object {
        const val KEY1 = "com.example.hackverse.SignInScreen.KEY1"
        const val KEY2 = "com.example.hackverse.SignInScreen.KEY2"
        const val KEY3 = "com.example.hackverse.SignInScreen.KEY3"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_screen)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // Ensure this is set in your strings.xml
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize views
        val toSignUpScreen = findViewById<TextView>(R.id.tosignup)
        val signInButton = findViewById<Button>(R.id.btnSignIn)
        val userId = findViewById<TextInputEditText>(R.id.userIdEditText)
        val userPass = findViewById<TextInputEditText>(R.id.userPassEditText)
        val googleSignInButton = findViewById<Button>(R.id.googlebtn)

        // Handle Sign In Button click
        signInButton.setOnClickListener {
            val userIdString = userId.text.toString().replace(".", ",")
            val passwordString = userPass.text.toString()

            if (userIdString.isNotEmpty() && passwordString.isNotEmpty()) {
                readData(userIdString, passwordString)
            } else {
                Toast.makeText(this, "Enter User Id and Password", Toast.LENGTH_LONG).show()
            }
        }

        // Navigate to Sign Up Screen
        toSignUpScreen.setOnClickListener {
            val newUserintent = Intent(this, SignUpScreen::class.java)
            startActivity(newUserintent)
        }

        // Google Sign-In button
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    // Check for Firebase Database user credentials
    private fun readData(userid: String, pass: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(userid).get().addOnSuccessListener {
            if (it.exists()) {
                val storedPassword = it.child("pass").value.toString()
                if (storedPassword == pass) {
                    Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show()

                    // Retrieve user info
                    val emailId = it.child("email").value.toString()
                    val nameId = it.child("name").value.toString()
                    val userId = it.child("userid").value.toString()

                    // Navigate to Main Screen and pass data
                    val intentMainScreen1 = Intent(this, MainScreen1::class.java)
                    intentMainScreen1.putExtra("name", nameId)
                    intentMainScreen1.putExtra("userId", userId)
                    intentMainScreen1.putExtra("emailId", emailId)
                    startActivity(intentMainScreen1)
                } else {
                    Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User Does not Exist, Sign Up First", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to read data from the database", Toast.LENGTH_SHORT).show()
        }
    }

    // Google Sign-In logic
    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Handle the result of the Google Sign-In intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In was successful, now authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign-In failed, handle the error
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Authenticate with Firebase using Google credentials
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        account?.let {
            val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        // Sign-in success, update UI with the signed-in user's info
                        val user: FirebaseUser? = mAuth.currentUser
                        Toast.makeText(this, "Authentication Success", Toast.LENGTH_SHORT).show()

                        // Navigate to Main Screen with Google user info
                        val intentMainScreen1 = Intent(this, MainScreen1::class.java)
                        intentMainScreen1.putExtra("name", user?.displayName)
                        intentMainScreen1.putExtra("userId", user?.uid)
                        intentMainScreen1.putExtra("emailId", user?.email)
                        startActivity(intentMainScreen1)
                    } else {
                        // If sign-in fails, display a message
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
