package com.example.jobpulse

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//this adapter class for displaying a list of posts in a RecyclerView.

class PostAdapter (private val context: Context, private val postList: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    //this viewholder class holds references to the views for each data item.
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descTextView: TextView = itemView.findViewById(R.id.descTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    //this will return the total number of items in the data set which is hold by adapter
    override fun getItemCount(): Int {
        return postList.size
    }

    //this method should update the content of the ViewHOlder.itemview to reflect the item at the given position
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = postList[position]
        holder.titleTextView.text = currentPost.title
        holder.descTextView.text = currentPost.desc
        holder.authorTextView.text = "Posted by: ${currentPost.authorName}"
        holder.timeTextView.text = "Posted on: ${currentPost.createdTime}"
        Log.i("pass","pass 0")

        //new
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        if (userId !== null) {
            Log.i("pass 1","pass 1")

            currentPost.authorId?.let { Log.i("currentPost.authorId", it) }
            var recruitmentRef = database.reference.child("recruitment").child(userId)
            recruitmentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.i("reffff","reffff")
                        if (dataSnapshot.child("connect").exists()) {
                            val connectArray: ArrayList<String> =
                                dataSnapshot.child("connect").value as ArrayList<String>
                            for(item in connectArray){
                                Log.i("conn",item)
                            }
                            if (!connectArray.contains(currentPost.authorId)) {
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
    }
}