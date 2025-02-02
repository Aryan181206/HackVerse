package com.example.hackverse

import DataStoreManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yourapp.TeamAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class team : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var teamAdapter: TeamAdapter
    private val userList = mutableListOf<teamUserdata>()
    private val db = FirebaseFirestore.getInstance()


    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team, container, false)

        val btnaddfriend = view.findViewById<Button>(R.id.addfriend)
        btnaddfriend.setOnClickListener {
            val intent = Intent(context, FriendDetail::class.java)
            startActivity(intent)
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        teamAdapter = TeamAdapter(userList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = teamAdapter

        // Access DataStoreManager to retrieve user data
        val dataStoreManager = DataStoreManager(requireContext())

        lifecycleScope.launch {
            val (name, email, uid) = dataStoreManager.getUserData().first()

            val path ="/Userdata/$uid/team"
            db.collection(path)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documentSnapshots = task.result // Get the QuerySnapshot result
                        if (documentSnapshots != null) {
                            for (i in documentSnapshots) { // Iterate over the list of DocumentSnapshots
                                val friendemail = i.getString("email") ?: ""
                                val friendname = i.getString("name") ?: ""

                                val frienddata = teamUserdata(
                                    name = friendname,
                                    email = friendemail
                                )
                                userList.add(frienddata)
                            }
                            teamAdapter.notifyDataSetChanged()
                        }
                    } else {
                        // Handle the error if the task failed
                        Log.e("LoadTeamMembers", "Error getting documents: ", task.exception)
                    }
                }
        }
        return view
    }
}


