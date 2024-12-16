package com.example.hackverse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DashboardRV1Adapter(private val dataList : List<hackathinviewdata>) : RecyclerView.Adapter<DashboardRV1Adapter.DashboardViewHolder>() {


    class DashboardViewHolder(hackathinviewdata: View) :
        RecyclerView.ViewHolder(hackathinviewdata) {
        val Title: TextView = itemView.findViewById(R.id.titlename)
        val Image: ImageView = itemView.findViewById(R.id.hackathonshowimage)

    }

    // Create a new ViewHolder when a new item view is needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.hackathonview, parent, false)
        return DashboardViewHolder(itemView)
    }


    // Bind the data to the views
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.Title.text = currentItem.Title
        holder.Image.setImageResource(currentItem.Image)

        // If you're using URLs for images
        Glide.with(holder.itemView.context)
            .load(currentItem.Image)
            .into(holder.Image) // Load the image into the ImageView

    }         // Return the number of items in the list
    override fun getItemCount(): Int {
        return dataList.size
    }

}