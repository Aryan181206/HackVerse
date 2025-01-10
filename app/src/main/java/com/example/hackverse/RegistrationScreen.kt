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


        dataStoreManager = DataStoreManager(applicationContext)

        val hackathonId = intent.getStringExtra("Hackathonclickedid")

        val teamSize = SelectedHackathon.hackathonData?.TeamSize?.toIntOrNull() ?: 0

        lifecycleScope.launch {
            dataStoreManager.getUserData().collect { userData ->
                val (name, email, uid) = userData
                val path = "/Userdata/$uid/team"
                db.collection(path)
                    .get()
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {

                            val documentSnapshots = task.result
                            if (documentSnapshots != null) {
                                for (i in documentSnapshots) {

                                    val friendname = i.getString("name") ?: ""
                                    val friendemail = i.getString("email") ?: ""
                                    teamMemberName.add(friendname)
                                    teamMemberEmail.add(friendemail)
                                }

                            }
                        }
                    }




                val btnregsiter = findViewById<Button>(R.id.regsiter)

                val spinnerContainer: LinearLayout = findViewById(R.id.registerspinner)

                for (i in 0 until teamSize - 1) {
                    val spinner = Spinner(this@RegistrationScreen)
                    val adapter = ArrayAdapter(
                        this@RegistrationScreen,
                        android.R.layout.simple_list_item_1,
                        teamMemberName
                    )
                    adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
                    spinner.adapter = adapter

                    spinnerContainer.addView(spinner)
                }
                btnregsiter.setOnClickListener {
                    val selectedMembers = mutableListOf<Map<String, String>>()
                    val selectedNames = mutableSetOf<String>()


                    for (i in 0 until teamSize - 1) {
                        val spinner = spinnerContainer.getChildAt(i) as Spinner
                        val selectedName = spinner.selectedItem.toString()

                        if (selectedName in selectedNames) {
                            Toast.makeText(
                                this@RegistrationScreen,
                                "Each team member must be unique. Duplicate: $selectedName",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }

                        selectedNames.add(selectedName)
                        val selectedEmail = teamMemberEmail[teamMemberName.indexOf(selectedName)]

                        selectedMembers.add(mapOf("name" to selectedName, "email" to selectedEmail))
                    }

                    selectedMembers.add(0, mapOf("name" to name, "email" to email))

                     val teamData = mapOf("teamMembers" to selectedMembers)

                    db.collection("AddedHackathonData")
                        .document("$hackathonId")
                        .collection("RegisteredTeams")
                        .add(teamData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@RegistrationScreen,
                                "Registration Successful! Please Give feedback.",
                                Toast.LENGTH_SHORT
                            ).show()
                            db.collection("AddedHackathonData")
                                .document("$hackathonId")
                                .update("registrationCount", FieldValue.increment(teamSize.toLong()))
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

                            showCongratulationsDialog()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@RegistrationScreen,
                                "Registration Failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    db.collection("Userdata")
                        .document("$uid")
                        .collection("Participated Hackathon")
                        .document(hackathonId ?: "")
                        .set(mapOf("hackathonId" to hackathonId))

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
                val intent = Intent(this@RegistrationScreen, feedbackscreen::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Close") { dialog, id ->
                dialog.dismiss()
                startActivity(Intent(this@RegistrationScreen, MainScreen1::class.java))
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

}
