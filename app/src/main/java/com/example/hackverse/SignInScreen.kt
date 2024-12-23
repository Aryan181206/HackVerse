package com.example.hackverse

import DataStoreManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class SignInScreen : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_screen)
// Initialize Firebase
        FirebaseApp.initializeApp(this)


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))  // Ensure this is set in your strings.xml
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize views
        val toSignUpScreen = findViewById<TextView>(R.id.tosignup)
        val signInButton = findViewById<Button>(R.id.btnSignIn)
        val emailId = findViewById<TextInputEditText>(R.id.emailIdEditText)
        val userPass = findViewById<TextInputEditText>(R.id.userPassEditText)
        val googleSignInButton = findViewById<Button>(R.id.googlebtn)
        val forgotPasswordText = findViewById<TextView>(R.id.forgotPasswordText)

        val dataStoreManager = DataStoreManager(this)

        // Handle Sign In Button click
        signInButton.setOnClickListener {
            val email = emailId.text.toString()
            val password = userPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Get the authenticated user's info
                            val user = mAuth.currentUser
                            val uid = user?.uid

                            // Fetch additional user details from Firestore
                            firestore.collection("Userdata").document(uid!!)
                                .get()
                                .addOnSuccessListener { document ->
                                    val name = document.getString("name")
                                    val email = document.getString("email")

                                    // Save data in DataStore
                                    lifecycleScope.launch {
                                        dataStoreManager.saveUserData(name ?: "", email ?: "", uid)
                                    }

                                    Toast.makeText(this, "Sign-In Successful", Toast.LENGTH_SHORT)
                                        .show()

                                    // Navigate to Main Screen
                                    startActivity(Intent(this, MainScreen1::class.java))

                                }.addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Failed to fetch user data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Sign-In Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to Sign Up Screen
        toSignUpScreen.setOnClickListener {
            val newUserIntent = Intent(this, SignUpScreen::class.java)
            startActivity(newUserIntent)
        }

        // Google Sign-In button
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Handle Forgot Password
        forgotPasswordText.setOnClickListener {
            val email = emailId.text.toString()
            if (email.isNotEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                this,
                                "Error: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Google Sign-In logic
    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
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
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        account?.let {
            val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = mAuth.currentUser
                        Toast.makeText(this, "Authentication Success", Toast.LENGTH_SHORT).show()

                        val dataStoreManager = DataStoreManager(this)
                        lifecycleScope.launch {
                            dataStoreManager.saveUserData(
                                user?.displayName ?: "",
                                user?.email ?: "",
                                user?.uid ?: ""
                            )
                        }

                        startActivity(Intent(this, MainScreen1::class.java))
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
