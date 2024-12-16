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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInScreen : AppCompatActivity() {
    lateinit var databaseReference: DatabaseReference
    companion object{
        const val KEY1="com.example.hackverse.SignInScreen.KEY1"
        const val KEY2="com.example.hackverse.SignInScreen.KEY2"
        const val KEY3="com.example.hackverse.SignInScreen.KEY3"
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toSignUpScreen = findViewById<TextView>(R.id.tosignup)
        val signInButton = findViewById<Button>(R.id.btnSignIn)
        val userId = findViewById<TextInputEditText>(R.id.userIdEditText)
        val userPass = findViewById<TextInputEditText>(R.id.userPassEditText)

        signInButton.setOnClickListener{
            val userIdString = userId.text.toString().replace(".",",")
            val passwordString = userPass.text.toString()

            if (userIdString.isNotEmpty()&& passwordString.isNotEmpty()){
                readData(userIdString,passwordString)
            }else{
                Toast.makeText(this,"Enter User Id  and Password",Toast.LENGTH_LONG).show()
            }

        }
        toSignUpScreen.setOnClickListener {
            val newUserintent = Intent(this,SignUpScreen::class.java)
            startActivity(newUserintent)
        }
    }

    private fun readData(userid: String , pass: String ){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(userid).get().addOnSuccessListener {
            if(it.exists()){
                val storedPassword = it.child("pass").value.toString()
                if (storedPassword == pass){
                    Toast.makeText(this,"Sign In Successful",Toast.LENGTH_SHORT).show()

                    // retriving name and user
                    val emailId = it.child("email").value.toString()
                    val nameId = it.child("name").value.toString()
                    val userId = it.child("userid").value.toString()

                    // navigating to main screem 1 and passing data
                    val intentMainScreen1 = Intent(this,MainScreen1::class.java)
                    intentMainScreen1.putExtra(KEY1,nameId)
                    intentMainScreen1.putExtra(KEY2,userId)
                    intentMainScreen1.putExtra(KEY3,emailId)
                    startActivity(intentMainScreen1)


                    } else{
                    Toast.makeText(this,"Incorrect Password",Toast.LENGTH_SHORT).show()
                }
            }else{
            Toast.makeText(this, "User Does not Exist , Sign Up First ", Toast.LENGTH_SHORT).show()
        }
        }.addOnFailureListener{
            Toast.makeText(this,"Failed To read Data from the database ", Toast.LENGTH_SHORT).show()
        }

    }

}