package com.example.hackverse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterforEventfragment(private val hackathonList : List<HackathonViewDataInEventRecycler>):
    RecyclerView.Adapter<AdapterforEventfragment.HackathonViewHolder>() {



    inner class HackathonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize the views from your CardView layout
        val titleTextView: TextView = itemView.findViewById(R.id.showtitle)
        val organisationTextView: TextView = itemView.findViewById(R.id.showorganisation)
        val startDateTextView: TextView = itemView.findViewById(R.id.showstartdate)
        val endDateTextView: TextView = itemView.findViewById(R.id.showstartdate2)
        val modeTextView: TextView = itemView.findViewById(R.id.showmode)
        val typeTextView: TextView = itemView.findViewById(R.id.showtype)
        val rewardTextView: TextView = itemView.findViewById(R.id.showreward)
        val teamSizeTextView: TextView = itemView.findViewById(R.id.showteamsize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackathonViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hackathonvieweventfragment, parent, false)
        return HackathonViewHolder(view)
    }



        override fun onBindViewHolder(holder: HackathonViewHolder, position: Int) {
            // Get the current HackathonData item
            val currentHackathon = hackathonList[position]

            // Bind data to the views
            holder.titleTextView.text = currentHackathon.title
            holder.organisationTextView.text = currentHackathon.organisation
            holder.startDateTextView.text = currentHackathon.startDate
            holder.endDateTextView.text = currentHackathon.endDate
            holder.modeTextView.text = currentHackathon.mode
            holder.typeTextView.text = currentHackathon.type
            holder.rewardTextView.text = currentHackathon.reward
            holder.teamSizeTextView.text = currentHackathon.teamSize



        }
    override fun getItemCount(): Int {
        return hackathonList.size // Return the size of the data list
    }


    }
