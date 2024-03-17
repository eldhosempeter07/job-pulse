package com.example.jobpulse

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddConnectionAdapter(options:FirebaseRecyclerOptions<Candidate>):
    FirebaseRecyclerAdapter<Candidate, AddConnectionAdapter.MyViewHolder>(options){

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
        if(userId !== null){
            var recruitmentRef = database.reference.child("recruitment").child(userId)
        recruitmentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.child("connect").exists()){
                        val connectArray: ArrayList<String> =
                        dataSnapshot.child("connect").value as ArrayList<String>
                        if(connectArray.contains(model.id)){
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
            intent.putExtra("from", "add_candidate")
            holder.itemView.context.startActivity(intent)
        }
        val addFieldButton: Button = holder.itemView.findViewById(R.id.connect_button)
        addFieldButton.setOnClickListener {
            Toast.makeText(holder.itemView.getContext(), "Connected Successfully", Toast.LENGTH_LONG).show()
            if (userId != null) {
                addFieldToDocument(userId, model.id, holder)
            }
        }
    }

    private fun addFieldToDocument(userID: String, newValue: String,holder:MyViewHolder) {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var usersRef: DatabaseReference = database.reference.child("recruitment")
        val documentRef = usersRef.child(userID)

//         Check if the field already exists
        documentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userData = dataSnapshot.value as? Map<String, Any>
                    val fieldArray = userData?.get("connect") as? ArrayList<String>
                    if (fieldArray != null) {
                        // If the field already exists as an array, append the new value
                        fieldArray.add(newValue)
                        documentRef.child("connect").setValue(fieldArray)
                        val intent = Intent(holder.itemView.context, CandidateActivity::class.java)
                        Toast.makeText(holder.itemView.context, "Connected", Toast.LENGTH_SHORT).show()
                        holder.itemView.context.startActivity(intent)
                    } else {
                        // If the field does not exist, create it with the new value as an array
                        val newArray = arrayListOf(newValue)
                        documentRef.child("connect").setValue(newArray)
                        Toast.makeText(holder.itemView.context, "Connected", Toast.LENGTH_SHORT).show()
                        val intent = Intent(holder.itemView.context, CandidateActivity::class.java)
                        holder.itemView.context.startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("error","DatabaseError")
                // Handle cancellation
            }
        })
    }

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_connection,parent,false)){
        val name: TextView = itemView.findViewById(R.id.name)
        val title: TextView = itemView.findViewById(R.id.texttitle)
        val imageItem: ImageView = itemView.findViewById(R.id.imageItem)
    }
}