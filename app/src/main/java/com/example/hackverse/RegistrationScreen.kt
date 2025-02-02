package com.example.hackverse

import DataStoreManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.launch

class RegistrationScreen : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var dataStoreManager: DataStoreManager
    private var teamMemberName = mutableListOf("")
    private var teamMemberEmail = mutableListOf("")

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_screen)

        // Initialize the DataStoreManager
        dataStoreManager = DataStoreManager(applicationContext)

        // Retrieve data passed via intent
        val hackathonId = intent.getStringExtra("Hackathonclickedid")


        // Get selected hackathon data
        val teamSize = SelectedHackathon.hackathonData?.TeamSize?.toIntOrNull() ?: 0

        // Launch a coroutine to retrieve data from DataStore
        lifecycleScope.launch {
            dataStoreManager.getUserData().collect { userData ->
                val (name, email, uid) = userData
                if(!uid.isNullOrEmpty()) {
                    val path = "Userdata/$uid/team"
                    db.collection(path)
                        .get()
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {

                                val documentSnapshots = task.result // Get the QuerySnapshot result
                                if (documentSnapshots != null) {
                                    for (i in documentSnapshots) { // Iterate over the list of DocumentSnapshots

                                        val friendname = i.getString("name") ?: ""
                                        val friendemail = i.getString("email") ?: ""
                                        teamMemberName.add(friendname)
                                        teamMemberEmail.add(friendemail)
                                    }

                                }
                            }
                        }
                }




                val btnregsiter = findViewById<Button>(R.id.regsiter)
                // Spinner container
                val spinnerContainer: LinearLayout = findViewById(R.id.registerspinner)

                // Populate spinners dynamically based on team size
                for (i in 0 until teamSize - 1) {
                    val spinner = Spinner(this@RegistrationScreen)
                    val adapter = ArrayAdapter(
                        this@RegistrationScreen,
                        android.R.layout.simple_list_item_1,
                        teamMemberName
                    )
                    adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
                    spinner.adapter = adapter

                    // Add spinner to the container
                    spinnerContainer.addView(spinner)
                }
                btnregsiter.setOnClickListener {
                    //created a lostto hold the selected members details
                    val selectedMembers = mutableListOf<Map<String, String>>()
                    // Loop through each spinner to get the selected names

                    for (i in 0 until teamSize - 1) {
                        val spinner = spinnerContainer.getChildAt(i) as Spinner
                        val selectedName = spinner.selectedItem.toString()
                        val selectedEmail = teamMemberEmail[teamMemberName.indexOf(selectedName)]


                        // Add the name and email to the selected members list
                        selectedMembers.add(mapOf("name" to selectedName, "email" to selectedEmail))
                    }


                    selectedMembers.add(0, mapOf("name" to name, "email" to email))
                    // Store the selected names and emails in Firestore
                    val teamData = mapOf("teamMembers" to selectedMembers)

                    // Add team data to "RegisteredTeams" in the selected hackathon
                    db.collection("AddedHackathonData")
                        .document("$hackathonId")
                        .collection("RegisteredTeams")
                        .add(teamData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@RegistrationScreen,
                                "Registration Successful ! Please Give feedback",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Update the registration count of the selected hackathon
                            db.collection("AddedHackathonData")
                                .document("$hackathonId")
                                .update(
                                    "registrationCount",
                                    FieldValue.increment(teamSize.toLong())
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@RegistrationScreen,
                                        "Registration count updated!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@RegistrationScreen,
                                        "Failed to update registration count: $e",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            // Registration successful, show a congratulatory dialog
                            showCongratulationsDialog()

                        }.addOnFailureListener {
                            Toast.makeText(
                                this@RegistrationScreen,
                                "Registration Failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    // Add the hackathon details to the current user's "Participated Hackathon"
                    db.collection("Userdata")
                        .document("$uid")
                        .collection("Participated Hackathon")
                        .document(hackathonId ?: "")
                        .set(mapOf("hackathonId" to hackathonId))

                    // Add the hackathon details to each selected team member's "Participated Hackathon"

                    for (member in selectedMembers) {
                        val memberEmail = member["email"] ?: ""
                        db.collection("Userdata")
                            .whereEqualTo("email", memberEmail)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                val memberDocument = querySnapshot.documents.firstOrNull()
                                if (memberDocument != null) {
                                    val memberUid = memberDocument.id
                                    db.collection("Userdata")
                                        .document(memberUid)
                                        .collection("Participated Hackathon")
                                        .document(hackathonId ?: "")
                                        .set(mapOf("hackathonId" to hackathonId))
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@RegistrationScreen,
                                    "Failed to add hackathon for team member: $memberEmail",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }
        }
    }

    private fun showCongratulationsDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Congratulations! You Registered Successfully")
            .setMessage("You have successfully registered for the hackathon.")
            .setCancelable(false)
            .setPositiveButton("Give Feedback") { dialog, id ->
                // Navigate to the feedback screen when "Give Feedback" is clicked
                val intent = Intent(this@RegistrationScreen, feedbackscreen::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Close") { dialog, id ->
                // Close the dialog
                dialog.dismiss()
                startActivity(Intent(this@RegistrationScreen, MainScreen1::class.java))
            }

        // Create the AlertDialog and show it
        val alert = dialogBuilder.create()
        alert.show()
    }
}
