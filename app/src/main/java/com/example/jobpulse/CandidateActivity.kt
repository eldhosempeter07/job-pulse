package com.example.jobpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobpulse.MainActivity
import com.example.jobpulse.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CandidateActivity: AppCompatActivity()  {
    private var adapter:CandidateAdapter? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)
        val btn2: ImageButton = findViewById(R.id.imageButton2)
        btn2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        val btn4: ImageButton = findViewById(R.id.imageButton4)
        btn4.setOnClickListener {
            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)}

        val query = FirebaseDatabase.getInstance().reference.child("recruitment")
        val options = FirebaseRecyclerOptions.Builder<Candidate>().setQuery(query,Candidate::class.java).build()
        adapter = CandidateAdapter(options)
        val rview: RecyclerView = findViewById(R.id.rView)
        rview.layoutManager = LinearLayoutManager(this)
        rview.adapter = adapter

        val signout: ImageButton = findViewById(R.id.imageButton1)
        signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // Clear the back stack to prevent the user from going back
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
            startActivity(intent)
        }

        val back: ImageView = findViewById(R.id.arrowback)
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val addCandidate: Button = findViewById(R.id.addNew)
        addCandidate.setOnClickListener {
            val addConnectionIntent = Intent(this, AddConnectionActivity::class.java)
            startActivity(addConnectionIntent)
        }
    }

    override fun onStart(){
        super.onStart()
        adapter?.startListening()
    }
}