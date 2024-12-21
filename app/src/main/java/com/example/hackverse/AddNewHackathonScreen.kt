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


    //date piker

    private lateinit var editTextStartDate: EditText
    private lateinit var editTextEndDate: EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_hackathon_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Get references to views
        val spinnerTeamSize: Spinner = findViewById(R.id.spinnerteamsize)

        //team size options
        val teamSizes = arrayOf("Select Team Size", "1", "2", "3", "4", "5", "6")
        val mode = arrayOf("Select Mode", "Online", "Offline")


        // Create an adapter to populate the spinner
        val adapterforteamsize = ArrayAdapter(this, android.R.layout.simple_spinner_item, teamSizes)
        adapterforteamsize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTeamSize.adapter = adapterforteamsize


        // Handle item selection in spinner
        spinnerTeamSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                // Handle item selection if necessary
            }


            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle if nothing is selected
            }
        }


        // Set the button click listener

        // Reference the Spinner by ID
        val spinnerMode: Spinner = findViewById(R.id.spinnerMode)

        // Define the array of modes
        val modes = arrayOf("Select Mode", "Online", "Offline")

        // Create an ArrayAdapter to bind the modes to the Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modes)

        // Set the dropdown layout style
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the Spinner
        spinnerMode.adapter = adapter

        // Set an OnItemSelectedListener to handle the selection event
        spinnerMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                // Get the selected mode
                val selectedMode = parentView?.getItemAtPosition(position).toString()

                // Perform action based on selected mode
                if (selectedMode != "Select Mode") {
                    //Toast.makeText(this, "Selected Mode: $selectedMode", Toast.LENGTH_SHORT).show()
                    // You can replace this with any action based on the selected mode
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case when no item is selected
            }
        }


        // spinnner reward

        // Reference the Spinner by ID
        val spinnerReward: Spinner = findViewById(R.id.spinnerReward)

        // Define the array of modes
        val reward = arrayOf("Update Reward", "2000$", "1000$", "500$", "250$", "100$", "No Reward")

        // Create an ArrayAdapter to bind the modes to the Spinner
        val adapterforReward = ArrayAdapter(this, android.R.layout.simple_spinner_item, reward)

        // Set the dropdown layout style
        adapterforReward.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the Spinner
        spinnerReward.adapter = adapterforReward

        // Set an OnItemSelectedListener to handle the selection event
        spinnerReward.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                // Get the selected mode
                val selectedReward = parentView?.getItemAtPosition(position).toString()

                // Perform action based on selected mode
                if (selectedReward != "Update Reward") {
                    //Toast.makeText(this, "Selected Mode: $selectedMode", Toast.LENGTH_SHORT).show()
                    // You can replace this with any action based on the selected mode
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case when no item is selected
            }
        }


        // for type of hackathon paid or free

        // Reference the Spinner by ID
        val spinnertype: Spinner = findViewById(R.id.spinnertype)

        // Define the array of modes
        val type = arrayOf("Regestion Type", "Paid", "Free")

        // Create an ArrayAdapter to bind the modes to the Spinner
        val adapterfortype = ArrayAdapter(this, android.R.layout.simple_spinner_item, type)

        // Set the dropdown layout style
        adapterfortype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the Spinner
        spinnertype.adapter = adapterfortype

        // Set an OnItemSelectedListener to handle the selection event
        spinnertype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                // Get the selected mode
                val selectedType = parentView?.getItemAtPosition(position).toString()

                // Perform action based on selected mode
                if (selectedType != "Update Reward") {
                    //Toast.makeText(this, "Selected Mode: $selectedMode", Toast.LENGTH_SHORT).show()
                    // You can replace this with any action based on the selected mode
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Handle the case when no item is selected
            }
        }


        // Get references to the EditText views
        editTextStartDate = findViewById<EditText>(R.id.editTextStartDate)
        editTextEndDate = findViewById<EditText>(R.id.editTextEndDate)

        // Set OnClickListener for Start Date EditText
        editTextStartDate.setOnClickListener {
            showDatePickerDialog(true) // true means it's for Start Date
        }

        // Set OnClickListener for End Date EditText
        editTextEndDate.setOnClickListener {
            showDatePickerDialog(false) // false means it's for End Date
        }


        // collecting the all data of added hackathon in a arrary list

        // Inside your onCreate method
        val btn = findViewById<Button>(R.id.addhakathondata)
        btn.setOnClickListener {
            // Get the selected data only when the user clicks the button
            val HackathonTitle = findViewById<EditText>(R.id.HackathonTitle)
            val OrganisationName = findViewById<EditText>(R.id.Organisation)
            val teamSize = spinnerTeamSize.selectedItem.toString()
            val HackathonMode = spinnerMode.selectedItem.toString()
            val HackathonReward = spinnerReward.selectedItem.toString()
            val HackathonType = spinnertype.selectedItem.toString()
            val HackathonstartDate = editTextStartDate.text.toString()
            val HackathonendDate = editTextEndDate.text.toString()
            val Hackathondescription = findViewById<EditText>(R.id.description)

            // Simple validation for missing data
            if (HackathonTitle.text.isEmpty() || OrganisationName.text.isEmpty() || teamSize == "Select Team Size" ||
                HackathonMode == "Select Mode" || HackathonReward == "Update Reward" || HackathonType == "Regestion Type" ||
                HackathonstartDate.isEmpty() || HackathonendDate.isEmpty()
            ) {
                Toast.makeText(this, "Please provide all the details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Convert the date to a Firestore Timestamp (optional, if you want to store the date in Timestamp format)
            val calendar = Calendar.getInstance()
            val startDateParts = HackathonstartDate.split("/")
            val endDateParts = HackathonendDate.split("/")
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

            val HackathonData = hashMapOf(
                "HackathonTitle" to HackathonTitle.text.toString(),
                "OrganisationName" to OrganisationName.text.toString(),
                "TeamSize" to teamSize,
                "HackathonMode" to HackathonMode,
                "HackathonReward" to HackathonReward,
                "HackathonType" to HackathonType,
                "HackathonStartDate" to startCalendar.time, // Store as a Date object
                "HackathonEndDate" to endCalendar.time,     // Store as a Date object
                "HackathonDescription" to Hackathondescription.text.toString()
            )

            // Fetch all documents to determine the next ID
            db.collection("AddedHackathonData")
                .get()
                .addOnSuccessListener { documents ->
                    val nextDocId =
                        "Hackathon${documents.size() + 1}" // Increment based on the current size

                    // Add data with the custom document ID
                    db.collection("AddedHackathonData").document(nextDocId)
                        .set(HackathonData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Hackathon added successfully with ID: $nextDocId",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Navigate to the next screen
                            val intent = Intent(this, MainScreen1::class.java)
                            startActivity(intent)
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


        private fun showDatePickerDialog(isStartDate: Boolean) {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this, { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the selected date in dd/MM/yyyy format
                    val formattedDate =
                        String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)

                    // Set the selected date to the respective EditText
                    if (isStartDate) {
                        editTextStartDate.setText(formattedDate)
                    } else {
                        editTextEndDate.setText(formattedDate)
                    }
                },
                year, month, day
            )

            datePickerDialog.show()
        }
    }
