package com.example.hackverse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class savedHackathonadapter(
    private val hackathonList: List<savedHackathonData>,
    private val context: Context
) : RecyclerView.Adapter<savedHackathonadapter.SavedHackathonViewHolder>() {

    inner class SavedHackathonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.title)
        val organizerTextView: TextView = view.findViewById(R.id.organisedby)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedHackathonViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.eachitemsaved, parent, false)
        return SavedHackathonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedHackathonViewHolder, position: Int) {
        val hackathon = hackathonList[position]
        holder.titleTextView.text = hackathon.title
        holder.organizerTextView.text = hackathon.organizer

    }

    override fun getItemCount(): Int = hackathonList.size
}
