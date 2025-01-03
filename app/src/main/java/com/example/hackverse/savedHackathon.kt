package com.example.hackverse

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class savedHackathon : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: savedHackathonadapter
    private val hackathonList = mutableListOf<savedHackathonData>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_saved_hackathon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.RVsavedHackathon)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = savedHackathonadapter(hackathonList, this)
        recyclerView.adapter = adapter

        fetchSavedHackathons()





    }

    private fun fetchSavedHackathons() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("Userdata").document(uid).collection("savedHackathons")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val hackathonId = document.id
                    fetchHackathonDetails(hackathonId)
                }
            }.addOnFailureListener {
                Toast.makeText(this,"Failed to fetch saved Hackathon data",Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchHackathonDetails(hackathonId: String) {
        firestore
            .collection("AddedHackathonData")
            .document(hackathonId)
            .get()
            .addOnSuccessListener {
                    document ->
                if (document.exists()) {
                    val title = document.getString("HackathonTitle") ?: ""
                    val organizer = document.getString("OrganisationName") ?: ""

                    val hackathon = savedHackathonData(title, organizer)
                    hackathonList.add(hackathon)
                    adapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to fetch hackathon details.", Toast.LENGTH_SHORT).show()
            }
    }
}