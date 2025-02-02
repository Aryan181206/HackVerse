package com.example.hackverse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MentorAdapter(private var mentorList: List<MentorData>) :
    RecyclerView.Adapter<MentorAdapter.MentorViewHolder>() {

    class MentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.mentorName)
        private val experienceTextView: TextView = itemView.findViewById(R.id.mentorExperience)
        private val domainTextView: TextView = itemView.findViewById(R.id.mentorDomain)
       // private val emailTextView: TextView = itemView.findViewById(R.id.mentorEmail)
        //private val headlineTextView: TextView = itemView.findViewById(R.id.mentorHeadline)
        //private val contactTextView: TextView = itemView.findViewById(R.id.mentorContact)

        fun bind(mentor: MentorData) {
            nameTextView.text = mentor.name
            experienceTextView.text = mentor.workExperience
            domainTextView.text = mentor.industry
            //emailTextView.text = "Email: ${mentor.email}"
            //headlineTextView.text = "Headline: ${mentor.headline}"
            //contactTextView.text = "Contact: ${mentor.contact}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.eachitem_mentor, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentorList[position]
        holder.bind(mentor)

        // Set click listener to store data in SharedMentorData
        holder.itemView.setOnClickListener {
            SharedMentorData.selectedMentor = mentor
            val context: Context = holder.itemView.context
            context.startActivity(Intent(context, MentorDetailsActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return mentorList.size
    }

    fun updateMentorList(newList: List<MentorData>) {
        mentorList = newList
        notifyDataSetChanged()
    }
}
