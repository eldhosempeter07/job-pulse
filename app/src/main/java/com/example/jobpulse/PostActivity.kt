package com.example.jobpulse

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class PostActivity : AppCompatActivity() {

    private lateinit var titlePost : EditText
    private lateinit var descPost : EditText
    private lateinit var postButton: Button

    private val database = FirebaseDatabase.getInstance()
    private val postRef = database.getReference("posts")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        val currentUser = FirebaseAuth.getInstance().currentUser

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


        //Initializing UI element
        titlePost = findViewById(R.id.titlePost)
        descPost = findViewById(R.id.descPost)
        postButton = findViewById(R.id.postButton)

        // adding click listener event to the post button
        postButton.setOnClickListener{
            postNew()
        }
    }

    private fun postNew(){
        val title = titlePost.text.toString()
        val desc = descPost.text.toString()
        val currentUser = FirebaseAuth.getInstance().currentUser

        //Fetching user's name from Firebase Authentication
        val user = FirebaseAuth.getInstance().currentUser
        val authorName = user?.email

        val currentTime = Calendar.getInstance().time
        val post = Post(title, desc, currentUser?.uid , authorName, currentTime)


        // It will generate unique ID for the new post in firebase
        val postId = postRef.push().key
        postId?.let {
            postRef.child(it).setValue(post)

            //Navigate back to the main activity after posting
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }
}