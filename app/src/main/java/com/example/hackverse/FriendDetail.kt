package com.example.hackverse

import DataStoreManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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


        val dataStoreManager = DataStoreManager(this)
        lifecycleScope.launch {
            val (name, email, uid) = dataStoreManager.getUserData().first()

            addButton.setOnClickListener {
                val name = friendNameEditText.text.toString()
                val email = friendEmailEditText.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty()) {
                    checkEmailInFirebaseAuth(uid, name, email)
                } else {
                    Toast.makeText(this@FriendDetail, "Please enter valid details", Toast.LENGTH_SHORT).show()
                }
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


                // Now add the user's data to the friend's team collection
                addUserToFriendTeam(uid, email)

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


    private fun addUserToFriendTeam(uid: String, friendEmail: String) {
        // Retrieve the user's data
        val db = FirebaseFirestore.getInstance()

        // Get the user's data from Firestore
        db.collection("Userdata")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()){
                    val userData = document.data
                    if(userData!=null){
                        val userName = userData["name"]as? String ?: "Unknown"
                        val userEmail = userData["email"]as? String ?: "Unknown"
                        val userTeamData = hashMapOf(
                            "name" to userName,
                            "email" to userEmail
                        )
                        // Add the user's data to the friend's team collection
                        val friendEmailDocumentId = friendEmail.replace(".",",")
                        db.collection("Userdata")
                            .whereEqualTo("email",friendEmail)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                val friendDocument = querySnapshot.documents.firstOrNull()
                                if (friendDocument !=null) {
                                    val friendUid = friendDocument.id
                                    db.collection("Userdata")
                                        .document(friendUid)
                                        .collection("team")
                                        .document(uid)
                                        .set(userTeamData)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this,
                                                "Your data added to friend's team successfully!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                this,
                                                "Error adding your data to friend's team: ${it.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }else{
                                    Toast.makeText(
                                        this,
                                        "Friend not found in Firestore",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                            }

                    }
                }
            }.addOnFailureListener{
                Toast.makeText(
                    this,
                    "Error fetching user data: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }
}


