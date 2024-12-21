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
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class FriendDetail : AppCompatActivity() {


    private lateinit var friendNameEditText: EditText
    private lateinit var friendEmailEditText: EditText
    private lateinit var addButton: Button
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_friend_detail)


        friendNameEditText = findViewById(R.id.friendName)
        friendEmailEditText = findViewById(R.id.friendEmail)
        addButton = findViewById(R.id.add)





        addButton.setOnClickListener {
            val name = friendNameEditText.text.toString()
            val email = friendEmailEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {

                val uid = intent.getStringExtra("uiddata") ?: ""
                if (uid.isEmpty()) {
                    Toast.makeText(this, "UID is missing", Toast.LENGTH_SHORT).show()
                    finish() // Close activity if no UID is found
                }

                checkEmailInFirebaseAuth(uid, name, email)
            } else {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkEmailInFirebaseAuth(uid: String, name: String, email: String) {
        // Get reference to Firestore
        val firestore = FirebaseFirestore.getInstance()
            firestore.collection("Userdata")
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Check if the email exists in the collection
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        // If the email exists, proceed with adding to the user collection
                        addToUserCollection(uid, name, email)
                    } else {
                        Toast.makeText(
                            this,
                            "User email not found in Firebase Authentication",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error checking user: ${it.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
    private fun addToUserCollection(uid: String, name: String, email: String) {
        val teamMemberData = hashMapOf(
            "name" to name,
            "email" to email
        )
        val db = FirebaseFirestore.getInstance()

        // Ensure the friend email is replaced properly for the document ID
        val emailDocumentId = email.replace(".", ",")

        db.collection("Userdata")
            .document(uid)
            .collection("team")
            .document(emailDocumentId)
            .set(teamMemberData)
            .addOnSuccessListener {
                Toast.makeText(this, "Friend added to team successfully!", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, MainScreen1::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Error adding friend to team: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}


