package com.example.hackverse

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MentorDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_details)

        // Access the selected mentor from the shared object
        val mentor = SharedMentorData.selectedMentor

        if (mentor != null) {
            findViewById<TextView>(R.id.mentorDetailsName).text = mentor.name
            findViewById<TextView>(R.id.mentorDetailscurrentorganisation).text = mentor.organisation
            findViewById<TextView>(R.id.mentorDetailsExperience).text =mentor.workExperience
            findViewById<TextView>(R.id.mentorDetailsIndustry).text = mentor.industry
            findViewById<TextView>(R.id.mentorDetailsHeadline).text = mentor.headline
            findViewById<TextView>(R.id.mentorDetailslanguage).text = mentor.language
            findViewById<TextView>(R.id.mentorDetailsContact).text = mentor.contact
            findViewById<TextView>(R.id.mentorDetailsEmail).text =mentor.email

        }
    }
}
