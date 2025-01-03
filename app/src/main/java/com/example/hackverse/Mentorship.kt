package com.example.hackverse

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class Mentorship : Fragment() {

    private lateinit var mentorRecyclerView: RecyclerView
    private lateinit var mentorAdapter: MentorAdapter
    private lateinit var mentorList: ArrayList<MentorData>
    private lateinit var searchView: SearchView
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
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_mentorship, container, false)


        // Initialize mentor list
        mentorList = ArrayList()

        // Initialize adapter with empty list
        mentorAdapter = MentorAdapter(mentorList)

        mentorRecyclerView = view.findViewById(R.id.RVmentor)
        searchView = view.findViewById(R.id.mentorsearch)



        mentorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mentorRecyclerView.adapter = mentorAdapter

        fetchMentorsFromFirestore()

        setupSearchView()


        return view
    }

    private fun fetchMentorsFromFirestore() {
        firestore.collection("Mentor")
            .get()
            .addOnSuccessListener { result ->
                mentorList.clear()
                for (document in result) {
                    val mentor = document.toObject(MentorData::class.java)
                    mentor.mentorId = document.id
                    mentorList.add(mentor)
                }
                mentorAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch mentors", Toast.LENGTH_SHORT)
                    .show()
            }

    }
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchMentors(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchMentors(it) }
                return true
            }
        })
    }
    private fun searchMentors(query: String) {
        val filteredList = mentorList.filter { mentor ->
            mentor.name.contains(query, ignoreCase = true)
        }
        mentorAdapter.updateMentorList(filteredList)
    }


}