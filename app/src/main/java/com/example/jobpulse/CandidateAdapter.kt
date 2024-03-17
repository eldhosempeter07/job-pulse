package com.example.jobpulse

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobpulse.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.math.log

class CandidateAdapter(options:FirebaseRecyclerOptions<Candidate>):
    FirebaseRecyclerAdapter<Candidate, CandidateAdapter.MyViewHolder>(options){
    private lateinit var currentUserConnect: MutableList<String>
    private lateinit var user1Connect: MutableList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater,parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Candidate) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        holder.name.text = model.name
        holder.title.text = model.title
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        if(userId !== null) {
            var recruitmentRef = database.reference.child("recruitment").child(userId)
            recruitmentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("connect").exists()) {
                            val connectArray: ArrayList<String> =
                                dataSnapshot.child("connect").value as ArrayList<String>
                            if (!connectArray.contains(model.id)) {
                                holder.itemView.visibility = View.INVISIBLE
                                holder.itemView.layoutParams.height = 0
                                holder.itemView.layoutParams.width = 0
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }


        val storageRef:StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.image)
        Glide.with(holder.imageItem.context).load(storageRef).into(holder.imageItem)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CandidateDetailsActivity::class.java)
            // Pass data to the CandidateDetailsActivity
            intent.putExtra("name", model.name)
            intent.putExtra("title", model.title)
            intent.putExtra("description", model.description)
            intent.putExtra("image", model.image)
            intent.putExtra("id", model.id)
            intent.putExtra("from", "candidate")
            holder.itemView.context.startActivity(intent)
        }
        }

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_candidate,parent,false)){

        val name: TextView = itemView.findViewById(R.id.name)
        val title: TextView = itemView.findViewById(R.id.texttitle)
        val imageItem: ImageView = itemView.findViewById(R.id.imageItem)
    }
}