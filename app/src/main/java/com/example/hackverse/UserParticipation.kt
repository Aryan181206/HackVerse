package com.example.hackverse

import DataStoreManager
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserParticipation : AppCompatActivity() {
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var db: FirebaseFirestore
    private lateinit var hackathonAdapter: AdapterforUserparticipation
    private lateinit var hackathonsList: MutableList<UPHackathonData>


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_participation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize the DataStoreManager
        dataStoreManager = DataStoreManager(applicationContext)

        db = FirebaseFirestore.getInstance()

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.RVuserparticipation)
        recyclerView.layoutManager = LinearLayoutManager(this)
        hackathonsList = mutableListOf()
        hackathonAdapter = AdapterforUserparticipation(hackathonsList)
        recyclerView.adapter = hackathonAdapter

        lifecycleScope.launch {
            dataStoreManager.getUserData().collect() { userData ->
                val (name, email, uid) = userData
                db.collection("Userdata")
                    .document(uid)
                    .collection("Participated Hackathon")
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            // Loop through each registered hackathon to get details
                            for (document in documents) {
                                val hackathonId = document.getString("hackathonId") ?: continue
                                fetchHackathonDetails(hackathonId)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(
                            "UserParticipation",
                            "Error fetching registered hackathons",
                            exception
                        )
                    }
            }

        }


    }

    private fun fetchHackathonDetails(hackathonId: String) {
        db.collection("AddedHackathonData")
            .document(hackathonId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val hackathon = UPHackathonData(
                        HackathonId = hackathonId,
                        HackathonTitle = document.getString("HackathonTitle") ?: "",
                        OrganisationName = document.getString("OrganisationName") ?: "",
                        HackathonDescription = document.getString("HackathonDescription") ?: ""
                    )
                    hackathonsList.add(hackathon)
                    hackathonAdapter.notifyDataSetChanged() // Notify the adapter that data has changed
                }
            }
            .addOnFailureListener { exception ->
                Log.e("UserParticipation", "Error fetching hackathon details", exception)
            }
    }
}


