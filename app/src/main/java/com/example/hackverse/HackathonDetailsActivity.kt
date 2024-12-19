package com.example.hackverse

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class HackathonDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hackathon_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve the ArrayList from Intent
        val hackathonDetails = intent.getStringArrayListExtra("dataofhackathon")

        if (hackathonDetails!=null){
            val hackathonTitle: TextView = findViewById(R.id.nameofhackathon)
            val organisationName: TextView = findViewById(R.id.nameoforganisation)
            val hackathonMode: TextView = findViewById(R.id.modeofhackathon)
            val hackathonType: TextView = findViewById(R.id.showtype)
            val hackathonReward: TextView = findViewById(R.id.rewardofhackathon)
            val teamSize: TextView = findViewById(R.id.teamsizeofhackathon)

            hackathonTitle.text = hackathonDetails?.getOrNull(0) // HackathonTitle
            organisationName.text =  hackathonDetails?.getOrNull(1) // OrganisationName
            hackathonMode.text =  hackathonDetails?.getOrNull(2) // HackathonMode
            hackathonType.text =  hackathonDetails?.getOrNull(3) // HackathonType
            hackathonReward.text =  hackathonDetails?.getOrNull(4) // HackathonReward
            teamSize.text =  hackathonDetails?.getOrNull(5) // TeamSize

        }

    }
}