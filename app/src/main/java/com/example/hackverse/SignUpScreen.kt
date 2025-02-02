package com.example.hackverse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore


class SignUpScreen : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        //for firebase authentication

        val nameUser = findViewById<TextInputEditText>(R.id.etname)
        val emailUser = findViewById<TextInputEditText>(R.id.etemail)
        val passUser = findViewById<TextInputEditText>(R.id.etpassword)
        val registerButton = findViewById<Button>(R.id.btnCreate)
        val existingUser = findViewById<TextView>(R.id.tosignin)
        existingUser.setOnClickListener {
            val intentExist = Intent(this, SignInScreen::class.java)
            startActivity(intentExist)
        }

        registerButton.setOnClickListener {
            val name = nameUser.text.toString()
            val email = emailUser.text.toString()
            val pass = passUser.text.toString()


            // confirm that all is filled
            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() ) {

                // Create user with FirebaseAuth
                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Get the unique user ID (uid)
                            val uid = task.result?.user?.uid

                            // Store user data in Firestore
                            val user = hashMapOf(
                                "name" to name,
                                "email" to email
                            )

                            firestore.collection("Userdata").document(uid!!)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Account Created ! Now Sign In", Toast.LENGTH_SHORT)
                                        .show()
                                    // Navigate to MainScreen1
                                    val intentToSignIn = Intent(this, SignInScreen::class.java)
                                    startActivity(intentToSignIn)
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Failed to save user data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                "Registration Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }



            }

        }

    }
