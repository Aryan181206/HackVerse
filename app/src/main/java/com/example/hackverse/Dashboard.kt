package com.example.hackverse

import android.os.Bundle
import android.service.autofill.UserData
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackverse.SignInScreen.Companion.KEY1
import com.example.hackverse.SignInScreen.Companion.KEY2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener





class Dashboard : Fragment() {


    // Firebase Database reference
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference



    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DashboardRV1Adapter
    private lateinit var dataList: List<hackathinviewdata>

    lateinit var showname : TextView
    lateinit var showuserid : TextView



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
        showuserid =view.findViewById(R.id.showuserid)


        val data = arguments
        val name = data?.getString("name").toString()
        val userId = data?.getString("userid").toString()

        // Set the values to the TextViews
        showname.text = name
        showuserid.text = userId









        recyclerView = view.findViewById(R.id.recyclerviewindashboard)

        // Initialize data
        dataList = listOf(
            hackathinviewdata("John Doe",R.drawable.baseline_dashboard_24) ,// Example URL
            hackathinviewdata("Jane Smith", R.drawable.baseline_dashboard_24),
            hackathinviewdata("Aryan Sharma",R.drawable.baseline_dashboard_24),
            hackathinviewdata("Aryan Sharma",R.drawable.baseline_dashboard_24),
            hackathinviewdata("Aryan Sharma",R.drawable.baseline_dashboard_24)
        )


        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context ,LinearLayoutManager.HORIZONTAL, false)
        adapter = DashboardRV1Adapter(dataList)
        recyclerView.adapter = adapter




        return view
    }

    }
