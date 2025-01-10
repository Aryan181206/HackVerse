package com.example.hackverse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdapterforEventfragment(private val hackathonList : List<HackathonViewDataInEventRecycler>):
    RecyclerView.Adapter<AdapterforEventfragment.HackathonViewHolder>() {

    inner class HackathonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val titleTextView: TextView = itemView.findViewById(R.id.showtitle)
        val organisationTextView: TextView = itemView.findViewById(R.id.showorganisation)
        val startDateTextView: TextView = itemView.findViewById(R.id.showstartdate)
        val endDateTextView: TextView = itemView.findViewById(R.id.showstartdate2)
        val modeTextView: TextView = itemView.findViewById(R.id.showmode)
        val typeTextView: TextView = itemView.findViewById(R.id.showtype)
        val rewardTextView: TextView = itemView.findViewById(R.id.showreward)
        val teamSizeTextView: TextView = itemView.findViewById(R.id.showteamsize)
        val saveHackathonIcon: ImageView = itemView.findViewById(R.id.saveicon)
        val likeHackathonIcon: ImageView = itemView.findViewById(R.id.likeicon) // New like icon

        init {
            saveHackathonIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedHackathon = hackathonList[position]
                    saveHackathonToFirestore(selectedHackathon.HackathonId)
                }
            }
            likeHackathonIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedHackathon = hackathonList[position]
                    likeHackathon(selectedHackathon)
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedHackathon = hackathonList[position]

                    SelectedHackathon.hackathonData = selectedHackathon

                    val context = itemView.context
                    val intent = Intent(context, HackathonDetailsActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

        private fun saveHackathonToFirestore(hackathonId: String) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val userCollection = FirebaseFirestore.getInstance()
                    .collection("Userdata")
                    .document(userId)
                    .collection("savedHackathons")


                val hackathonData = hashMapOf(
                    "hackathonId" to hackathonId
                )
                userCollection.document(hackathonId).set(hackathonData)
                    .addOnSuccessListener {
                        Toast.makeText(
                            itemView.context,
                            "Hackathon saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { e ->
                        Toast.makeText(
                            itemView.context,
                            "Failied to save hacakathon ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(itemView.context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

        private fun likeHackathon(hackathon: HackathonViewDataInEventRecycler) {
            val firestore = FirebaseFirestore.getInstance()
            val hackathonRef =
                firestore.collection("AddedHackathonData").document(hackathon.HackathonId)
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val likedHackathonsRef = firestore.collection("Userdata")
                    .document(userId)
                    .collection("Liked Hackathons")

                // Check if the user has already liked the hackathon
                likedHackathonsRef.document(hackathon.HackathonId).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            // User has already liked this hackathon
                            Toast.makeText(
                                itemView.context,
                                "You have already liked this hackathon",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // User has not liked this hackathon yet, proceed to like
                            hackathonRef.get().addOnSuccessListener { hackathonDoc ->
                                if (hackathonDoc.exists()) {
                                    val currentLikes = hackathonDoc.getLong("likeCount") ?: 0
                                    hackathonRef.update("likeCount", currentLikes + 1)
                                } else {
                                    hackathonRef.set(mapOf("likeCount" to 1))
                                }

                                // Add the hackathon to the user's liked hackathons
                                val likedHackathonData = hashMapOf(
                                    "hackathonId" to hackathon.HackathonId,
                                    "hackathonTitle" to hackathon.HackathonTitle,
                                    "organisation" to hackathon.OrganisationName
                                )

                                likedHackathonsRef.document(hackathon.HackathonId)
                                    .set(likedHackathonData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            itemView.context,
                                            "Hackathon UpVoted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            itemView.context,
                                            "Failed to like hackathon",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            itemView.context,
                            "Failed to check like status",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(itemView.context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackathonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.hackathonvieweventfragment, parent, false)
            return HackathonViewHolder(view)
        }


        override fun onBindViewHolder(holder: HackathonViewHolder, position: Int) {

            val currentHackathon = hackathonList[position]


            holder.titleTextView.text = currentHackathon.HackathonTitle
            holder.organisationTextView.text = currentHackathon.OrganisationName
            holder.modeTextView.text = currentHackathon.HackathonMode
            holder.typeTextView.text = currentHackathon.HackathonType
            holder.rewardTextView.text = currentHackathon.HackathonReward
            holder.teamSizeTextView.text = currentHackathon.TeamSize
            holder.startDateTextView.text = currentHackathon.HackathonStartDate.toString()
            holder.endDateTextView.text = currentHackathon.HackathonEndDate.toString()


        }

        override fun getItemCount(): Int {
            return hackathonList.size
        }
    }

