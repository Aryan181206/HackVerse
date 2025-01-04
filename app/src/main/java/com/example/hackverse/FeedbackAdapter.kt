package com.example.hackverse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedbackAdapter : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    private val feedbackList = mutableListOf<Feedbackdata>()

    fun updateFeedbackList(newFeedbackList: List<Feedbackdata>) {
        feedbackList.clear()
        feedbackList.addAll(newFeedbackList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.eachfeedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        holder.bind(feedbackList[position])
    }

    override fun getItemCount(): Int = feedbackList.size

    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val organizationTextView: TextView = itemView.findViewById(R.id.organisation)
        private val feedbackTextView: TextView = itemView.findViewById(R.id.showfeedback)

        fun bind(feedback: Feedbackdata) {
            titleTextView.text = feedback.HackathonName
            organizationTextView.text = feedback.OrganisationName
            feedbackTextView.text = "Feedback: ${feedback.feedback}"
                   }
    }
}
