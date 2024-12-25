package com.example.hackverse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        val saveHackathonIcon: ImageView = itemView.findViewById(R.id.saveicon)
        //val hackathonImageView: ImageView = itemView.findViewById(R.id.hackathonImageView)


        init {
            saveHackathonIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedHackathon = hackathonList[position]
                    saveHackathonToFirestore(selectedHackathon.HackathonId)
                }
            }
        
            
            // Set an OnClickListener on the itemView
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedHackathon = hackathonList[position]

                    // Store the selected hackathon in the singleton
                    SelectedHackathon.hackathonData = selectedHackathon
                    // Start a new activity with details

                    // Navigate to the next activity
                    val context = itemView.context
                    val intent = Intent(context, HackathonDetailsActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

        private fun saveHackathonToFirestore(hackathonId: String) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null){
                val userCollection = FirebaseFirestore.getInstance()
                    .collection("Userdata")
                    .document(userId)
                    .collection("savedHackathons")

                val hackathonData = hashMapOf(
                    "hackathonId" to hackathonId
                )
                userCollection.add(hackathonData)
                    .addOnSuccessListener {
                        Toast.makeText(itemView.context, // Access the context from RecyclerView
                            "Hackathon saved successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { e->
                        Toast.makeText(itemView.context,"Failied to save hacakathon ", Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(itemView.context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
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
            holder.titleTextView.text = currentHackathon.HackathonTitle
            holder.organisationTextView.text = currentHackathon.OrganisationName
            holder.modeTextView.text = currentHackathon.HackathonMode
            holder.typeTextView.text = currentHackathon.HackathonType
            holder.rewardTextView.text = currentHackathon.HackathonReward
            holder.teamSizeTextView.text = currentHackathon.TeamSize
            holder.startDateTextView.text = currentHackathon.HackathonStartDate.toString()
            holder.endDateTextView.text = currentHackathon.HackathonEndDate.toString()

            //Glide.with(holder.itemView.context)
              //  .load(currentHackathon.imageUrl)
              //  .placeholder(R.drawable.placeholder) // Optional placeholder
                //.error(R.drawable.error_image) // Optional error image
                //.into(holder.hackathonImageView)



        }
    override fun getItemCount(): Int {
        return hackathonList.size // Return the size of the data list
    }


    }
