package com.example.hackverse

import DataStoreManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch


class Dashboard() : Fragment() {

    private lateinit var dataStoreManager: DataStoreManager

    // Firebase Database reference
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference


    private lateinit var recyclerView: RecyclerView

    lateinit var showname: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            ?.let {
            }

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = (inflater.inflate(R.layout.fragment_dashboard, container, false))



        showname = view.findViewById(R.id.showname)
        dataStoreManager = DataStoreManager(requireContext())
        lifecycleScope.launch {
            dataStoreManager.getUserData().collect { userData ->
                val (name, email, uid) = userData
                // Set the values to the TextViews
                showname.text = name
            }
        }

        val addfriend = view.findViewById<CardView>(R.id.addfriend)
        addfriend.setOnClickListener {
            val intent = Intent(context, FriendDetail::class.java)
            startActivity(intent)
        }
        val bementor = view.findViewById<CardView>(R.id.beMentor)
        bementor.setOnClickListener {
            val intent = Intent(context, Becomementor::class.java)
            startActivity(intent)
        }
        val addhackathon = view.findViewById<CardView>(R.id.cardView16)
        addhackathon.setOnClickListener {
            val intent = Intent(context,AddNewHackathonScreen::class.java)
            startActivity(intent)
        }

        return view

    }
}

