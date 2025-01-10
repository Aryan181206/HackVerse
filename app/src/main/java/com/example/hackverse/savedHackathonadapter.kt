package com.example.hackverse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class savedHackathonadapter(
    private val hackathonList: MutableList<savedHackathonData>,
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

        holder.itemView.setOnLongClickListener {
            showDeleteConfirmationDialog(position)
            true
        }

    }

    override fun getItemCount(): Int = hackathonList.size


    private fun showDeleteConfirmationDialog(position: Int) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Delete Hackathon")
            .setMessage("Are you sure you want to delete this hackathon?")
            .setPositiveButton("Delete") { _, _ ->
                val n = hackathonList[position] // Get the hackathon object


                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    // Reference to the Firestore path
                    val firestore = FirebaseFirestore.getInstance()
                    val savedHackathonsRef = firestore.collection("Userdata")
                        .document(userId)
                        .collection("savedHackathons")
                        .document("Hackathon1")

                    // Delete the document from Firestore
                    savedHackathonsRef.delete()
                        .addOnSuccessListener {
                            // Remove the item from the list
                            hackathonList.removeAt(position)
                            // Notify the adapter that the item is removed
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, hackathonList.size)

                            Toast.makeText(
                                context,
                                "Hackathon deleted successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Failed to delete hackathon: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }







}
