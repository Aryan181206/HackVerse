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
        val endDateTextView: TextView = findViewById(R.id.detailEndDate)
        val modeTextView: TextView = findViewById(R.id.detailMode)
        val typeTextView: TextView = findViewById(R.id.detailType)
        val rewardTextView: TextView = findViewById(R.id.detailReward)
        val teamSizeTextView: TextView = findViewById(R.id.detailTeamSize)
        val totalcountTextView: TextView = findViewById(R.id.detailCount)
        val reg =findViewById<Button>(R.id.register)


        //val hackathonImageView: ImageView = findViewById(R.id.detailImageView)


        // Get data from intent
        val title = intent.getStringExtra("HackathonTitle")
        val organisation = intent.getStringExtra("OrganisationName")
        val startDate = intent.getStringExtra("StartDate")
        val endDate = intent.getStringExtra("EndDate")
        val mode = intent.getStringExtra("HackathonMode")
        val type = intent.getStringExtra("HackathonType")
        val reward = intent.getStringExtra("HackathonReward")
        val teamSize = intent.getStringExtra("TeamSize")
        val hackathonid = intent.getStringExtra("HackathonId")
        val totalcount = intent.getStringExtra("TotalCount")

        //val imageUrl = intent.getStringExtra("ImageUrl")


        reg.setOnClickListener {
            val intent = Intent(this, RegistrationScreen::class.java)
            intent.putExtra("Hackathonclickedid",hackathonid)  // importent line

            startActivity(intent)
        }


        // Set data to views
        titleTextView.text = title
        organisationTextView.text = organisation
        startDateTextView.text = startDate
        endDateTextView.text = endDate
        modeTextView.text = mode
        typeTextView.text = type
        rewardTextView.text = reward
        teamSizeTextView.text = teamSize
        totalcountTextView.text = totalcount




        //Glide.with(this)
          //  .load(imageUrl)
            //.placeholder(R.drawable.placeholder)
            //.error(R.drawable.error_image)
            //.into(hackathonImageView)


    }
}
