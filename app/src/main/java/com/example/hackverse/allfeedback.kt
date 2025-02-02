package com.example.hackverse

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

class allfeedback : AppCompatActivity() {
    private lateinit var feedbackRecyclerView: RecyclerView
    private lateinit var feedbackAdapter: FeedbackAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_allfeedback)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        feedbackRecyclerView = findViewById(R.id.RVfeedback)
        feedbackRecyclerView.layoutManager = LinearLayoutManager(this)
        feedbackAdapter = FeedbackAdapter()
        feedbackRecyclerView.adapter = feedbackAdapter

        loadFeedbacks()

    }

    private fun loadFeedbacks() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("Userdata")
            .document(userId)
            .collection("feedbacks")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val feedbackList = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Feedbackdata::class.java)
                }
                feedbackAdapter.updateFeedbackList(feedbackList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading feedbacks: ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }
}