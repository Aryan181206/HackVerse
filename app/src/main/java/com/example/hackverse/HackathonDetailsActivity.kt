package com.example.hackverse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment


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


        // Initialize views
        val titleTextView: TextView = findViewById(R.id.detailTitle)
        val organisationTextView: TextView = findViewById(R.id.detailOrganisation)
        val startDateTextView: TextView = findViewById(R.id.detailStartDate)
        val startDateTextView2: TextView = findViewById(R.id.detailstartDate2)
        val endDateTextView: TextView = findViewById(R.id.detailEndDate)
        val modeTextView: TextView = findViewById(R.id.detailMode)
        val typeTextView: TextView = findViewById(R.id.detailType)
        val rewardTextView: TextView = findViewById(R.id.detailReward)
        val teamSizeTextView: TextView = findViewById(R.id.detailTeamSize)
        val totalcountTextView: TextView = findViewById(R.id.detailCount)
        val likecountTextView: TextView = findViewById(R.id.detaillikecount)

        val reg : Button = findViewById(R.id.btnregister)


        //val hackathonImageView: ImageView = findViewById(R.id.detailImageView)


        val hackathonData = SelectedHackathon.hackathonData

        //val imageUrl = intent.getStringExtra("ImageUrl")



        reg?.setOnClickListener {
            val intent = Intent(this, RegistrationScreen::class.java)
            if (hackathonData != null) {
                intent.putExtra("Hackathonclickedid", hackathonData.HackathonId)
            }  // importent line

            startActivity(intent)
        }


        if (hackathonData != null) {
            // Set data to views
            titleTextView.text = hackathonData.HackathonTitle
            organisationTextView.text = hackathonData.OrganisationName
            startDateTextView.text = hackathonData.HackathonStartDate
            startDateTextView2.text = hackathonData.HackathonStartDate
            endDateTextView.text = hackathonData.HackathonEndDate
            modeTextView.text = hackathonData.HackathonMode
            typeTextView.text = hackathonData.HackathonType
            rewardTextView.text = hackathonData.HackathonReward
            teamSizeTextView.text = hackathonData.TeamSize
            totalcountTextView.text = hackathonData.TotalCount
            likecountTextView.text = hackathonData.likeCount

        }
    }
}
