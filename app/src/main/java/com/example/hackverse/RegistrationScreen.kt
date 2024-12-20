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
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationScreen : AppCompatActivity() {


    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var hackathonIdEditText: EditText // For example, ID of the hackathon
    private lateinit var registerButton: Button
    private val db = FirebaseFirestore.getInstance()




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration_screen)



        val hackathonid = intent.getStringExtra("Hackathonclickedid")







        // Initialize views
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        hackathonIdEditText = findViewById(R.id.hackathonIdEditText)
        registerButton = findViewById(R.id.registerButton)



        // Register button click listener
        registerButton.setOnClickListener {

                // Get student data from EditText fields
                val studentName = nameEditText.text.toString()
                val studentEmail = emailEditText.text.toString()
                val studentuserid =hackathonIdEditText.text.toString()


                if (studentName.isEmpty() || studentEmail.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }


                // Create a new student registration map
                val studentdata = hashMapOf(
                    "userid" to studentuserid,
                    "name" to studentName,
                    "email" to studentEmail,
                )

            // Reference to the specific hackathon's RegistrationData subcollection
            val registrationPath = "/AddedHackathonData/$hackathonid/RegisteredStudentData"

                db.collection(registrationPath)
                    .document(studentuserid)
                    .set(studentdata)
                    .addOnSuccessListener {
                        Toast.makeText(this,"Registration successful!",Toast.LENGTH_SHORT).show()
                        if (hackathonid != null) {
                            updateRegistrationCount(hackathonid)
                        }
                    }
                    .addOnFailureListener{ e ->
                        Toast.makeText(this, "Error registering: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

            // after clicking go to dash board screen
            val i = Intent(this,MainScreen1::class.java)
            startActivity(i)
        }
    }
    private fun updateRegistrationCount(hackathonid: String) {
        // Reference to the hackathon document
        val hackathonRef = db.collection("AddedHackathonData").document(hackathonid)

        // Check if the document exists
        hackathonRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Document exists, update the registration count
                    hackathonRef.update("registrationCount", com.google.firebase.firestore.FieldValue.increment(1))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Hackathon registration count updated!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error updating count: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Document does not exist, create it and initialize registrationCount
                    hackathonRef.set(hashMapOf("registrationCount" to 1))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Hackathon document created and registration count initialized!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error creating document: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error checking document existence: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}






