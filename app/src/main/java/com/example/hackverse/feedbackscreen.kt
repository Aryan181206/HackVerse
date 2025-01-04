package com.example.hackverse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class feedbackscreen : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val hackathon = SelectedHackathon.hackathonData
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feedbackscreen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val submit = findViewById<Button>(R.id.submit)
        val feedtext = findViewById<EditText>(R.id.feedtext)
        submit.setOnClickListener {
            val feedbackText = feedtext.text.toString()

            if (feedbackText.isBlank()) {
                Toast.makeText(this, "Feedback cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val userId = auth.currentUser?.uid ?: "anonymous"
                val feedbackdetails = mapOf(
                    "HackathonId" to hackathon?.HackathonId,
                    "HackathonName" to hackathon?.HackathonTitle,
                    "OrganisationName" to hackathon?.OrganisationName,
                    "feedback" to feedbackText)

                firestore.collection("Userdata")
                    .document(userId)
                    .collection("feedbacks")
                    .add(feedbackdetails)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT)
                            .show()
                        feedtext.text.clear()
                        val intent = Intent(this@feedbackscreen,MainScreen1::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this,"Failed to submit feedback",Toast.LENGTH_SHORT).show()
                    }

            }
        }


    }

}