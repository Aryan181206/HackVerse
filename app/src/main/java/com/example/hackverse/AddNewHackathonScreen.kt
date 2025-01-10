package com.example.hackverse

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AddNewHackathonScreen : AppCompatActivity() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var editTextStartDate: EditText
    private lateinit var editTextEndDate: EditText
    private var startDateMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_hackathon_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinner(
            R.id.spinnerteamsize,
            arrayOf("Select Team Size", "1", "2", "3", "4", "5", "6")
        )

        setupSpinner(
            R.id.spinnerMode,
            arrayOf("Select Mode", "Online", "Offline")
        )

        setupSpinner(
            R.id.spinnerReward,
            arrayOf("Update Reward", "2000$", "1000$", "500$", "250$", "100$", "No Reward")
        )

        setupSpinner(
            R.id.spinnertype,
            arrayOf("Regestion Type", "Paid", "Free")
        )

        editTextStartDate = findViewById(R.id.editTextStartDate)
        editTextEndDate = findViewById(R.id.editTextEndDate)

        editTextStartDate.setOnClickListener {
            showDatePickerDialog(true)
        }

        editTextEndDate.setOnClickListener {
            showDatePickerDialog(false)
        }

        val btn = findViewById<Button>(R.id.addhakathondata)
        btn.setOnClickListener { addHackathonData() }
    }

    private fun setupSpinner(spinnerId: Int, items: Array<String>) {
        val spinner: Spinner = findViewById(spinnerId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate = String.format(
                    "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear
                )

                if (isStartDate) {
                    if (selectedCalendar.timeInMillis < calendar.timeInMillis) {
                        Toast.makeText(this, "Start date must be today or later", Toast.LENGTH_SHORT).show()
                    } else {
                        editTextStartDate.setText(formattedDate)
                        startDateMillis = selectedCalendar.timeInMillis
                    }
                } else {
                    if (startDateMillis == 0L) {
                        Toast.makeText(this, "Please select the start date first", Toast.LENGTH_SHORT).show()
                    } else if (selectedCalendar.timeInMillis < startDateMillis) {
                        Toast.makeText(this, "End date must be after the start date", Toast.LENGTH_SHORT).show()
                    } else {
                        editTextEndDate.setText(formattedDate)
                    }
                }
            },
            year, month, day
        )

        datePickerDialog.show()
    }


    private fun addHackathonData() {
        val hackathonTitle = findViewById<EditText>(R.id.HackathonTitle)
        val organisationName = findViewById<EditText>(R.id.Organisation)
        val teamSize = findViewById<Spinner>(R.id.spinnerteamsize).selectedItem.toString()
        val hackathonMode = findViewById<Spinner>(R.id.spinnerMode).selectedItem.toString()
        val hackathonReward = findViewById<Spinner>(R.id.spinnerReward).selectedItem.toString()
        val hackathonType = findViewById<Spinner>(R.id.spinnertype).selectedItem.toString()
        val hackathonStartDate = editTextStartDate.text.toString()
        val hackathonEndDate = editTextEndDate.text.toString()
        val hackathonDescription = findViewById<EditText>(R.id.description)

        if (hackathonTitle.text.isEmpty() || organisationName.text.isEmpty() ||
            teamSize == "Select Team Size" || hackathonMode == "Select Mode" ||
            hackathonReward == "Update Reward" || hackathonType == "Regestion Type" ||
            hackathonStartDate.isEmpty() || hackathonEndDate.isEmpty()
        ) {
            Toast.makeText(this, "Please provide all the details", Toast.LENGTH_SHORT).show()
            return
        }

        val startDateParts = hackathonStartDate.split("/")
        val endDateParts = hackathonEndDate.split("/")
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        startCalendar.set(
            startDateParts[2].toInt(),
            startDateParts[1].toInt() - 1,
            startDateParts[0].toInt()
        )
        endCalendar.set(
            endDateParts[2].toInt(),
            endDateParts[1].toInt() - 1,
            endDateParts[0].toInt()
        )

        val hackathonData = hashMapOf(
            "HackathonTitle" to hackathonTitle.text.toString(),
            "OrganisationName" to organisationName.text.toString(),
            "TeamSize" to teamSize,
            "HackathonMode" to hackathonMode,
            "HackathonReward" to hackathonReward,
            "HackathonType" to hackathonType,
            "HackathonStartDate" to startCalendar.time,
            "HackathonEndDate" to endCalendar.time,
            "HackathonDescription" to hackathonDescription.text.toString()
        )

        db.collection("AddedHackathonData")
            .get()
            .addOnSuccessListener { documents ->
                val nextDocId = "Hackathon${documents.size() + 1}"
                db.collection("AddedHackathonData").document(nextDocId)
                    .set(hackathonData)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Hackathon added successfully with ID: $nextDocId",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, MainScreen1::class.java))
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Some error occurred: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error fetching documents: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
