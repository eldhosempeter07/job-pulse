package com.example.jobpulse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CandidateDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Extract data from the intent
        val candidateName = intent.getStringExtra("name")
        val candidateDescrip = intent.getStringExtra("description")
        val candidateTitle = intent.getStringExtra("title")
        val candidateImage = intent.getStringExtra("image")



        val imgDetail: ImageView = findViewById(R.id.imgDetail)
        val nameDetail: TextView = findViewById(R.id.nameDetail)
        val descrip: TextView = findViewById(R.id.descrip)
        val detailTitle: TextView = findViewById(R.id.detailTitle)
        val addToConnect: Button = findViewById(R.id.AddToConnect)
        val back: ImageView = findViewById(R.id.arrowback)

        back.setOnClickListener {
            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)
        }
        val home: ImageButton = findViewById(R.id.imageButton1)
        home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        if(candidateImage != null){
            val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(candidateImage)
            Glide.with(this).load(storageRef).into(imgDetail)

        }

        nameDetail.text = candidateName
        detailTitle.text = candidateTitle
        descrip.text = candidateDescrip

        addToConnect.setOnClickListener {
            Toast.makeText(this@CandidateDetailsActivity, "Connected Successfully", Toast.LENGTH_SHORT).show()
        }
    }

}
