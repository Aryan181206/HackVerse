package com.example.hackverse

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Becomementor : AppCompatActivity() {


    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_becomementor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        // Initialize Views
        val nameEditText = findViewById<EditText>(R.id.name)
        val emailEditText = findViewById<EditText>(R.id.email)
        val genderSpinner = findViewById<Spinner>(R.id.gender)
        val domainSpinner = findViewById<Spinner>(R.id.domain)
        val languageSpinner = findViewById<Spinner>(R.id.language)
        val currentOrganisationEditText = findViewById<EditText>(R.id.currentorganisation)
        val workExperienceEditText = findViewById<EditText>(R.id.WorkExperience)
        val headlineEditText = findViewById<EditText>(R.id.Headline)
        val contactEditText =findViewById<EditText>(R.id.contact)
        val submitButton = findViewById<Button>(R.id.Submit)

        // Pre-fill name and email
        val currentUser = auth.currentUser
        if (currentUser != null) {
            nameEditText.setText(currentUser.displayName)
            emailEditText.setText(currentUser.email)
        }

        // Setup Spinners
        setupSpinner(genderSpinner, "Select Gender", listOf("Male", "Female", "Other"))
        setupSpinner(
            domainSpinner,
            "Select Industry",
            listOf(
                "Technology",
                "Finance",
                "Healthcare",
                "Education",
                "Manufacturing",
                "Retail and E-commerce",
                "Hospitality and Tourism",
                "Energy and Utilities",
                "Media and Entertainment",
                "Telecommunications",
                "Real Estate",
                "Agriculture and Food Production",
                "Aerospace and Defense",
                "Automotive",
                "Fashion and Apparel",
                "Sports and Recreation")
        )
        setupSpinner(
            languageSpinner,
            "Select Language",
            listOf("English", "Hindi", "Spanish", "French")
        )


        // Handle Submit Button Click
        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val gender = genderSpinner.selectedItem.toString()
            val organisation = currentOrganisationEditText.text.toString()
            val domain = domainSpinner.selectedItem.toString()
            val workExperience = workExperienceEditText.text.toString()
            val headline = headlineEditText.text.toString()
            val language = languageSpinner.selectedItem.toString()
            val contact = contactEditText.text.toString()

            if (name.isBlank() || email.isBlank() || gender.isBlank() || organisation.isBlank() || domain.isBlank() ||workExperience.isBlank()|| headline.isBlank()||language.isBlank()) {
                Toast.makeText(this, "fill all Details ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            saveMentorData(name, email, gender, organisation, domain, workExperience, headline, language, contact)

            val intent = Intent (this , MainScreen1::class.java)
            startActivity(intent)
        }
    }

    private fun saveMentorData(
        name: String,
        email: String,
        gender: String,
        organisation: String,
        domain: String,
        workExperience: String,
        headline: String,
        language: String,
        contact : String

    ) {
        // Generate custom mentor ID
        val mentorCollection = firestore.collection("Mentor")
        mentorCollection.get().addOnSuccessListener { snapshot ->
            val mentorId = "mentor${snapshot.size() + 1}"

            val mentorData = hashMapOf(
                "name" to name,
                "email" to email,
                "gender" to gender,
                "organisation" to organisation,
                "contact" to contact,
                "industry" to domain,
                "workExperience" to workExperience,
                "headline" to headline,
                "language" to language
            )

            mentorCollection.document(mentorId).set(mentorData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun setupSpinner(spinner: Spinner?, question: String, listOf: List<String>) {
        // Combine the hint with the list of items
        val spinnerItems = listOf(question) + listOf

        val adapter = object : ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, spinnerItems
        ) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item (the question)
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as android.widget.TextView
                // Set the hint item text color
                textView.setTextColor(
                    if (position == 0) android.graphics.Color.GRAY
                    else android.graphics.Color.BLACK
                )
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        if (spinner != null) {
            spinner.adapter = adapter
        }

        spinner?.setSelection(0) // Default selection to the hint item
    }




}
