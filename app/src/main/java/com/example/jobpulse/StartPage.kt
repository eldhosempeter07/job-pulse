package com.example.jobpulse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class StartPage : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_start_page)

        textView = findViewById(R.id.User_Email)
        button = findViewById(R.id.logout)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        btnStart = findViewById(R.id.btnStart)

        if (user == null) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            textView.text = user.email
        }

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        btnStart.setOnClickListener {
            val intent = Intent(applicationContext, CandidateActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
