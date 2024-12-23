package com.example.hackverse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterforUserparticipation(private val hackathons: List<UPHackathonData>) :
    RecyclerView.Adapter<AdapterforUserparticipation.HackathonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackathonViewHolder {
        // Inflate the item_hackathon layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_participation_item, parent, false)
        return HackathonViewHolder(view)
    }

    override fun onBindViewHolder(holder: HackathonViewHolder, position: Int) {
        // Get the hackathon data at the given position
        val hackathon = hackathons[position]
        // Bind the hackathon data to the view holder
        holder.bind(hackathon)
    }

    override fun getItemCount(): Int = hackathons.size // Return the size of the list

    // ViewHolder class to hold the views for each item in the RecyclerView
    inner class HackathonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.hackathonName)
        private val OrganisationTextView: TextView = itemView.findViewById(R.id.hackathonOrganisation)

        // Bind the data to the views
        fun bind(hackathon: UPHackathonData) {
            nameTextView.text = hackathon.HackathonTitle
            OrganisationTextView.text = hackathon.OrganisationName
        }
    }
}
