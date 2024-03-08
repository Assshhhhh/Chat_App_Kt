package com.example.chatappkt.activities.registeration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.chatappkt.activities.MainActivity
import com.example.chatappkt.databinding.ActivitySignupBinding
import com.example.chatappkt.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        binding.tvMoveToLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
        }

        binding.buttonSignup.setOnClickListener {
            val name = binding.editName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            when {
                name.isEmpty() -> {
                    Toast.makeText(this@SignupActivity, "Please Enter Your Name", Toast.LENGTH_SHORT).show()
                    binding.editName.error = "Name is Required!"
                }
                email.isEmpty() -> {
                    Toast.makeText(this@SignupActivity, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
                    binding.editEmail.error = "Email is Required!"
                }
                password.isEmpty() -> {
                    Toast.makeText(this@SignupActivity, "Please Enter Your Password", Toast.LENGTH_SHORT).show()
                    binding.editPassword.error = "Password is Required!"
                }
                else -> {

                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@SignupActivity) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@SignupActivity, "Account Signed Up", Toast.LENGTH_SHORT).show()
                                binding.editName.setText("")
                                binding.editEmail.setText("")
                                addToDatabase(name, email, mAuth.currentUser?.uid)
                                finish()
                                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))

                            } else {
                                Toast.makeText(this@SignupActivity, "Failed! Try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener(this@SignupActivity) { e ->
                            Toast.makeText(this@SignupActivity, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun addToDatabase(name: String, email: String, uid: String?) {
        val userData = UserModel(name, email, uid!!)
        dbRef.child(uid).setValue(userData).addOnCompleteListener {
            if (it.isSuccessful){
                //Toast.makeText(this@SignupActivity, "Data inserted successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this@SignupActivity, "Failed to add data", Toast.LENGTH_SHORT).show()
            }
        }

    }
}