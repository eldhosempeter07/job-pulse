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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddConnectionActivity : AppCompatActivity() {
    private var adapter:AddConnectionAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connection)

        val signout: ImageButton = findViewById(R.id.signout_button)
        signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // Clear the back stack to prevent the user from going back
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
            startActivity(intent)
        }

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
