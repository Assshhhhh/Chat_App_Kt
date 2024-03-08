package com.example.chatappkt.activities.registeration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.chatappkt.activities.MainActivity
import com.example.chatappkt.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.tvMoveToSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }

        // Shared Prefs
        mAuth.addAuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser
            if (user != null) {
                try {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    // Handle exception if needed
                }
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editLoginEmail.text.toString().trim()
            val password = binding.editLoginPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    Toast.makeText(this@LoginActivity, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
                    binding.editLoginEmail.error = "Email is Required!"
                }
                password.isEmpty() -> {
                    Toast.makeText(this@LoginActivity, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
                    binding.editLoginPassword.error = "Password is Required!"
                }
                else -> {
                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@LoginActivity) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                                binding.editLoginEmail.setText("")
                                binding.editLoginPassword.setText("")
                                finish()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                            } else {
                                Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener(this@LoginActivity) { e ->
                            Toast.makeText(this@LoginActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Reset Password", Toast.LENGTH_SHORT).show()
        }
    }
}