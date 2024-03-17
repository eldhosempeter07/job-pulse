package com.example.jobpulse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CandidateDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val user = FirebaseAuth.getInstance().currentUser

        // Extract data from the intent
        val candidateName = intent.getStringExtra("name")
        val candidateDescrip = intent.getStringExtra("description")
        val candidateTitle = intent.getStringExtra("title")
        val candidateImage = intent.getStringExtra("image")
        val candidateId = intent.getStringExtra("id")
        val from = intent.getStringExtra("from")

        val imgDetail: ImageView = findViewById(R.id.imgDetail)
        val nameDetail: TextView = findViewById(R.id.nameDetail)
        val descrip: TextView = findViewById(R.id.descrip)
        val detailTitle: TextView = findViewById(R.id.detailTitle)
        val addToConnect: Button = findViewById(R.id.AddToConnect)
        val back: ImageView = findViewById(R.id.arrowback)

        if(from == "candidate"){
            addToConnect.visibility = View.INVISIBLE
            addToConnect.visibility = View.GONE
        }else{
            if (user != null && candidateId != null) {
                val userId = user.uid
            addToConnect.setOnClickListener {
                Toast.makeText(this@CandidateDetailsActivity, "Connected Successfully", Toast.LENGTH_SHORT).show()
                    addFieldToDocument(userId,candidateId)

                }
            }
        }

        back.setOnClickListener {
            val intent = Intent(this, CandidateActivity::class.java)
            startActivity(intent)
        }

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

        if(candidateImage != null){
            val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(candidateImage)
            Glide.with(this).load(storageRef).into(imgDetail)

        }
        nameDetail.text = candidateName
        detailTitle.text = candidateTitle
        descrip.text = candidateDescrip
    }

    private fun addFieldToDocument(userID: String, newValue: String) {
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
                        val intent = Intent(this@CandidateDetailsActivity, CandidateActivity::class.java)
                        Toast.makeText(this@CandidateDetailsActivity, "Connected Successfully", Toast.LENGTH_SHORT).show()
                        this@CandidateDetailsActivity.startActivity(intent)
                    } else {
                        // If the field does not exist, create it with the new value as an array
                        val newArray = arrayListOf(newValue)
                        documentRef.child("connect").setValue(newArray)
                        Toast.makeText(this@CandidateDetailsActivity, "Connected Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CandidateDetailsActivity, CandidateActivity::class.java)
                        this@CandidateDetailsActivity.startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("error","DatabaseError")
                // Handle cancellation
            }
        })
    }


}
