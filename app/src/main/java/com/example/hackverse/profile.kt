package com.example.hackverse

import DataStoreManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class profile() : Fragment() {
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var firebaseAuth: FirebaseAuth


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
        val touserparticipation = view.findViewById<CardView>(R.id.toparticipation)
        touserparticipation.setOnClickListener {
            val intent = Intent(context, UserParticipation::class.java)
            startActivity(intent)
        }

        val tosaved = view.findViewById<CardView>(R.id.tosaved)
        tosaved.setOnClickListener{
            val intent = Intent(context,savedHackathon::class.java)
            startActivity(intent)
        }

        val tofeedback = view.findViewById<CardView>(R.id.tofeedback)
        tofeedback.setOnClickListener{
            val intent = Intent(context,allfeedback::class.java)
            startActivity(intent)
        }

        // Set up the logout button click listener
        val logoutImage = view.findViewById<ImageView>(R.id.logoutimg)
        logoutImage.setOnClickListener {
            showLogoutConfirmationDialog()
        }
        return view
    }

    private fun showLogoutConfirmationDialog() {
        // Create an AlertDialog to confirm the logout action
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to log out?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                // Handle Firebase logout
                logoutFromFirebase()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
            }
        val alert = builder.create()
        alert.show()
    }

    private fun logoutFromFirebase() {
        if (!::firebaseAuth.isInitialized) {
            firebaseAuth = FirebaseAuth.getInstance()
        }
        //Perform Firebase logout
        firebaseAuth.signOut()

        //Show a toast message
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        //navigate to the login screen (or another appropriate screen)
        val intent = Intent(context, SignInScreen::class.java)
        startActivity(intent)

        //clear any stored user data from DataStore or SharedPreferences
        lifecycleScope.launch {
            dataStoreManager.clearUserData()
        }

        // Finish the profile fragment or the current activity
        activity?.finish()
    }
}
