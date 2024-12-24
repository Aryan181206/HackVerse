package com.example.hackverse

import DataStoreManager
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
        val bottomNavigationbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationbar.selectedItemId = R.id.Dashboard
        replacewithfragemt(Dashboard())

        bottomNavigationbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.Team -> replacewithfragemt(team())
                R.id.Event -> replacewithfragemt(Event())
                R.id.Dashboard -> replacewithfragemt(Dashboard())
                R.id.Mentorship -> replacewithfragemt(Mentorship())
                R.id.Profile -> replacewithfragemt(profile())
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