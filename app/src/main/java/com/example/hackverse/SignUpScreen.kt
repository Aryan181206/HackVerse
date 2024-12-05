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
        }

        val nameUser = findViewById<TextInputEditText>(R.id.etname)
        val emailUser = findViewById<TextInputEditText>(R.id.etemail)
        val passUser = findViewById<TextInputEditText>(R.id.etpassword)
        val useridUser = findViewById<TextInputEditText>(R.id.etuserid)
        val registerButton = findViewById<Button>(R.id.btnCreate)

        registerButton.setOnClickListener{
            val name = nameUser.text.toString()
            val email = emailUser.text.toString().replace(".",",")
            val pass = passUser.text.toString()
            val userid = useridUser.text.toString()

            val user = User(name,email,pass,userid)
            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(userid).setValue(user).addOnSuccessListener {
                Toast.makeText(this,"Account Created ",Toast.LENGTH_SHORT).show()
                val intentToMain1 = Intent(this,MainScreen1::class.java )
                startActivity(intentToMain1)
            }.addOnFailureListener{
                Toast.makeText(this,"Account Creation failed ",Toast.LENGTH_SHORT).show()
            }


        }

        val existingUser = findViewById<TextView>(R.id.tosignin)
        existingUser.setOnClickListener {
            val intentExist = Intent(this,SignInScreen :: class.java)
            startActivity(intentExist)
        }




    }
}