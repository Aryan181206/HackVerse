package com.example.hackverse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpScreen : AppCompatActivity() {
    private lateinit var database: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }//for firebase authentication

        val nameUser = findViewById<TextInputEditText>(R.id.etname)
        val emailUser = findViewById<TextInputEditText>(R.id.etemail)
        val passUser = findViewById<TextInputEditText>(R.id.etpassword)
        val useridUser = findViewById<TextInputEditText>(R.id.etuserid)
        val registerButton = findViewById<Button>(R.id.btnCreate)

        registerButton.setOnClickListener {
            val name = nameUser.text.toString()
            val email = emailUser.text.toString().replace(".", ",")
            val pass = passUser.text.toString()
            val userid = useridUser.text.toString()

            // confirm that all is filled
            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && userid.isNotEmpty()) {

                //obtain data
                val user = User(name, email, pass, userid)
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.child(userid).get().addOnSuccessListener {
                    if (it.exists()) {
                        Toast.makeText(
                            this,
                            "User Id must be Unique Already exits",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // writing the data into firebase database
                        database.child(userid).setValue(user).addOnSuccessListener {
                            Toast.makeText(this, "Account Created ", Toast.LENGTH_SHORT).show()

                            //navigating to mainscreen 1 when account created
                            val intentToMain1 = Intent(this, MainScreen1::class.java)
                            intentToMain1.putExtra("name", name)
                            intentToMain1.putExtra("email", email)
                            intentToMain1.putExtra("pass", pass)
                            intentToMain1.putExtra("userId", userid)
                            startActivity(intentToMain1)

                        }.addOnFailureListener {
                            Toast.makeText(this, "Account Creation failed ", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Provide All Details", Toast.LENGTH_SHORT).show()
            }
        }

        val existingUser = findViewById<TextView>(R.id.tosignin)
        existingUser.setOnClickListener {
            val intentExist = Intent(this, SignInScreen::class.java)
            startActivity(intentExist)
        }

    }








}