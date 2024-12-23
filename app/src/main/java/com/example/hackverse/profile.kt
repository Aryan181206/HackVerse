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
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class profile() : Fragment() {
    private lateinit var dataStoreManager: DataStoreManager

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        dataStoreManager = DataStoreManager(requireContext())
        lifecycleScope.launch {
            dataStoreManager.getUserData().collect { userData ->
                val (name, email,uid) = userData
                val nameinprofile = view.findViewById<TextView>(R.id.nameinprofile)
                val nameinprofile2 = view.findViewById<TextView>(R.id.nameinprofile2)
                val emailinprofile = view.findViewById<TextView>(R.id.emailinprofile)
                nameinprofile.text = name
                nameinprofile2.text = name
                emailinprofile.text = email
            }

        }
        val touserparticipation = view.findViewById<Button>(R.id.toparticipation)
        touserparticipation.setOnClickListener {
            val intent = Intent(requireContext(), UserParticipation::class.java)
            startActivity(intent)
        }
        return view
    }
}