package com.example.hackverse

import DataStoreManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
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
    private lateinit var adapter: DashboardRV1Adapter
    private lateinit var dataList: List<hackathinviewdata>

    lateinit var showname: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            ?.let {
            }

    }

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


            recyclerView = view.findViewById(R.id.recyclerviewindashboard)

            // Initialize data
            dataList = listOf(
                hackathinviewdata("John Doe", R.drawable.baseline_dashboard_24),// Example URL
                hackathinviewdata("Jane Smith", R.drawable.baseline_dashboard_24),
                hackathinviewdata("Aryan Sharma", R.drawable.baseline_dashboard_24),
                hackathinviewdata("Aryan Sharma", R.drawable.baseline_dashboard_24),
                hackathinviewdata("Aryan Sharma", R.drawable.baseline_dashboard_24)
            )


            // Set up the RecyclerView
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = DashboardRV1Adapter(dataList)
            recyclerView.adapter = adapter




            return view

        }
    }

