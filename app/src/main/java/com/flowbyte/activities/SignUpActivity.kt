package com.flowbyte.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flowbyte.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var _binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        setupUI()
    }

    private fun setupUI() {
        _binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        _binding.buttonRegister.setOnClickListener {
            val username = _binding.editTextUsername.text.toString().trim()
            val email = _binding.editTextEmail.text.toString().trim()
            val password = _binding.editTextPassword.text.toString().trim()

            if (username.isEmpty()) {
                _binding.editTextUsername.error = "Username is required"
            } else if (email.isEmpty()) {
                _binding.editTextEmail.error = "Email is required"
            } else if (password.isEmpty()) {
                _binding.editTextPassword.error = "Password is required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _binding.editTextEmail.error = "Email address is not valid"
            } else {
                registerUser(username, email, password)
            }
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                auth.currentUser?.updateProfile(userProfileChangeRequest { displayName = username })
                showToast("Authentication success.")
                navigateToLogin()
            } else {
                showToast("Authentication failed.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
