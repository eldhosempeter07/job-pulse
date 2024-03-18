package com.example.jobpulse

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>

    //firebase database and reference to the "posts" node
    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn2: ImageButton = findViewById(R.id.imageButton2)
        btn2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        val btn3 : ImageButton = findViewById(R.id.imageButton3)
        btn3.setOnClickListener {
            val intent = Intent (this, PostActivity::class.java)
            startActivity(intent)
        }

        val btn4: ImageButton = findViewById(R.id.imageButton4)
        btn4.setOnClickListener {
            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)}


        //Initializing recyclerview and setting it's property
        recyclerView = findViewById(R.id.recyclerFeed)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Initializing postList and postAdapter
        postList = mutableListOf()
        postAdapter = PostAdapter(this, postList)
        recyclerView.adapter = postAdapter

        retrievePosts()
    }

    // this function is for retrieving posts from firebase and will update the UI
    private fun retrievePosts(){
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //It will clear the existing list and will add new posts from Firebase
                postList.clear()
                for (postSnapshot in dataSnapshot.children){
                    val post = postSnapshot.getValue(Post :: class.java)
                    post?.let {
                        postList.add(it)
                    }
                }
                postAdapter.notifyDataSetChanged() // Notify the adapter that the data set has changed
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error fetching Posts:")// Log Error if there is an error in fetching
            }

        })
    }
}