package com.example.hackverse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainScreen1 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }





        // obtaining some profile data from signup screen

        val namedata = intent.getStringExtra("name")
        val emaildata = intent.getStringExtra("email")



        // obtaining some profile data from signin screen
        val shownamedata = intent.getStringExtra("name")
        val showemaildata = intent.getStringExtra("emailId")


        val dashboardfragment = Dashboard(shownamedata.toString() , showemaildata.toString())

        val profilefragment = profile(namedata.toString(), emaildata.toString())



        val bottomNavigationbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationbar.selectedItemId = R.id.Dashboard
        replacewithfragemt(Dashboard(shownamedata.toString() , showemaildata.toString()))

        bottomNavigationbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.Team -> replacewithfragemt(team())
                R.id.Event -> replacewithfragemt(Event())
                R.id.Dashboard -> replacewithfragemt(Dashboard(shownamedata.toString() , showemaildata.toString()))
                R.id.Mentorship -> replacewithfragemt(Mentorship())
                R.id.Profile -> replacewithfragemt(profile(namedata.toString(),  emaildata.toString()))
                else -> {

                }
            }
            true
        }
    }

    private fun replacewithfragemt(fragment: Fragment) {
        val fragmentmanager = supportFragmentManager
        val fragmentTransition = fragmentmanager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout, fragment)
        fragmentTransition.commit()

    }
}