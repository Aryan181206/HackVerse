package com.example.hackverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackverse.SignInScreen.Companion.KEY1
import com.example.hackverse.SignInScreen.Companion.KEY2
import com.google.firebase.database.DatabaseReference


class Dashboard : Fragment() {



    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DashboardRV1Adapter
    private lateinit var dataList: List<hackathinviewdata>


    lateinit var showname: TextView
    lateinit var showuserId: TextView


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

        showname = view.findViewById<TextView>(R.id.showname)
        showuserId = view.findViewById<TextView>(R.id.showuserid)
        val data = arguments
        if (data != null) {
            val nameshow= data.getString("name").toString()
            showname.text = nameshow

        }else
            showname.text ="Aryan 9 Sharma"



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