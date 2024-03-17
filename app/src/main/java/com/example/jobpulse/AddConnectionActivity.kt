package com.example.jobpulse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class AddConnectionActivity : AppCompatActivity() {
    private var adapter:AddConnectionAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connection)

        val btn2: ImageButton = findViewById(R.id.home_buttom)
        btn2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }

        val btn4: ImageButton = findViewById(R.id.imageButton4)
        btn4.setOnClickListener {

            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)}

//        Adapter
        val query = FirebaseDatabase.getInstance().reference.child("recruitment")
        val options = FirebaseRecyclerOptions.Builder<Candidate>().setQuery(query,Candidate::class.java).build()
        adapter = AddConnectionAdapter(options)
        val rview: RecyclerView = findViewById(R.id.rView)
        rview.layoutManager = LinearLayoutManager(this)
        rview.adapter = adapter


//        Home Button
        val home: ImageButton = findViewById(R.id.home_buttom)
        home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

//        Back Button
        val back: ImageView = findViewById(R.id.arrowback)
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart(){
        super.onStart()
        adapter?.startListening()
    }
}
