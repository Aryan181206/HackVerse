package com.example.hackverse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


class Event : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var hackathonAdapter: AdapterforEventfragment
    private val filteredList: MutableList<HackathonViewDataInEventRecycler> = mutableListOf()
    private var hackathonList: MutableList<HackathonViewDataInEventRecycler> = mutableListOf()

    private val firestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)



        Log.d("HackathonData", "Data fetched: $hackathonList")
        recyclerView = view.findViewById(R.id.RVinEventScreen)
        val searchView = view.findViewById<SearchView>(R.id.searchview)


        //fetch data from firestore
        fetchDatafromFirestore()
        //set up recyclerview

        recyclerView.layoutManager = LinearLayoutManager(context)
        hackathonAdapter = AdapterforEventfragment(hackathonList)
        hackathonAdapter = AdapterforEventfragment(filteredList)
        recyclerView.adapter = hackathonAdapter


        val btnadd = view.findViewById<Button>(R.id.AddNewHackthon)
        btnadd.setOnClickListener {
            val intent = Intent(activity, AddNewHackathonScreen::class.java)
            startActivity(intent)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterHackathons(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterHackathons(it) }
                return true
            }
        })



        return view
    }

    private fun fetchDatafromFirestore() {
        firestore.collection("AddedHackathonData")
            .get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    for (document in documents) {


                        val Hackthonid = document.id
                        // Retrieve individual fields from the document
                        val hackathonDescription = document.getString("HackathonDescription") ?: ""
                        val hackathonEndDate = document.getTimestamp("HackathonEndDate")
                        val hackathonMode = document.getString("HackathonMode") ?: ""
                        val hackathonReward = document.getString("HackathonReward") ?: ""
                        val hackathonStartDate = document.getTimestamp("HackathonStartDate")
                        val hackathonTitle = document.getString("HackathonTitle") ?: ""
                        val hackathonType = document.getString("HackathonType") ?: ""
                        val organisationName = document.getString("OrganisationName") ?: ""
                        val teamSize = document.getString("TeamSize") ?: ""
                        val totalcount = document.getLong("registrationCount")
                        val likecount = document.getLong("likeCount").toString()

                        var count =totalcount.toString()





                        // Format the timestamps to strings
                        val formattedStartDate = formatTimestampToString(hackathonStartDate)
                        val formattedEndDate = formatTimestampToString(hackathonEndDate)


                        // Create a HackathonViewDataInEventRecycler object and populate it with values

                        val hackathon = HackathonViewDataInEventRecycler(
                            HackathonDescription = hackathonDescription,
                            HackathonEndDate = formattedEndDate,
                            HackathonMode = hackathonMode,
                            HackathonReward = hackathonReward,
                            HackathonStartDate = formattedStartDate,
                            HackathonTitle = hackathonTitle,
                            HackathonType = hackathonType,
                            OrganisationName = organisationName,
                            TeamSize = teamSize,
                            HackathonId = Hackthonid,
                            TotalCount = count,
                            likeCount = likecount


                        )


                        hackathonList.add(hackathon)
                    }

                    filteredList.addAll(hackathonList)
                    hackathonAdapter.notifyDataSetChanged() // Notify adapter that data has changed
                }
            }
            .addOnFailureListener {
            }
    }

        private fun formatTimestampToString(timestamp: Timestamp?): String {

            if (timestamp == null) {
                return ""
            }
            else{

                // Convert Timestamp to Date
                val date = timestamp.toDate()


                // Use SimpleDateFormat to format the Date to a String
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                return dateFormat.format(date)
            }
        }
    private fun filterHackathons(query: String) {
        val searchQuery = query.lowercase(Locale.getDefault())
        filteredList.clear()
        filteredList.addAll(hackathonList.filter {
            it.HackathonTitle.lowercase(Locale.getDefault()).contains(searchQuery)
        })
        hackathonAdapter.notifyDataSetChanged()
    }

}

