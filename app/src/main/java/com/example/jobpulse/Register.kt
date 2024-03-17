package com.example.jobpulse
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.regex.Matcher
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var textView: TextView
    private lateinit var mAuth: FirebaseAuth

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
        buttonReg = findViewById(R.id.btnregister)
        textView = findViewById(R.id.loginNow)
        editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()

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
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@Register,
                            "Account Created",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(applicationContext, Login::class.java)
                        startActivity(intent)
                        finish()
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
