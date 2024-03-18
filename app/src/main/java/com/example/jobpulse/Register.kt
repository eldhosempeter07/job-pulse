package com.example.jobpulse
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.regex.Matcher
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextauthername: TextInputEditText
    private lateinit var editTexttitle: TextInputEditText
    private lateinit var editTextdesc: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var textView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onStart() {     super.onStart()
        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextauthername = findViewById(R.id.authername)
        editTexttitle = findViewById(R.id.usertitle)
        editTextdesc = findViewById(R.id.userdesc)
        buttonReg = findViewById(R.id.btnregister)
        textView = findViewById(R.id.loginNow)
        editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("recruitment")
        textView.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener {
            val email: String = editTextEmail.text.toString()
            val password: String = editTextPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this@Register, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                Toast.makeText(
                    this@Register,
                    "Password must include at least one uppercase letter, one lowercase letter, and one digit.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser: FirebaseUser? = mAuth.currentUser
                        val userId = currentUser?.uid
                        // Save user data to the database
                        val desc = editTextdesc.text.toString()
                        val authorName = editTextauthername.text.toString()
                        val title = editTexttitle.text.toString()
                        val currentTime = Calendar.getInstance().time
                        val link = "gs://job-pulse-18b7c.appspot.com/candidate1.jpg"
                        val recruitment = Recruitment(title, desc, userId, authorName, currentTime,link)
                        userId?.let {
                            databaseReference.child(it).setValue(recruitment)
                                .addOnCompleteListener { dbTask ->
                                    if (dbTask.isSuccessful) {
                                        Toast.makeText(
                                            this@Register,
                                            "Account Created",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(applicationContext, Login::class.java)
                                        startActivity(intent)
                                        // Proceed to login or any other action
                                    } else {
                                        // Handle database save failure
                                        Toast.makeText(
                                            this@Register,
                                            "Failed to save user data",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(
                            this@Register,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"
        val pattern: Pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
