package com.flowbyte.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flowbyte.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val SHARED_PREFS: String = "sharedPrefs"
    private val REMEMBER_ME: String = "rememberMe"
    private lateinit var _binding: ActivityLoginBinding
    private lateinit var checkbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        checkbox = _binding.checkbox

        // Check if the user is already logged in
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val isRemembered = sharedPreferences.getBoolean(REMEMBER_ME, false)

        if (isRemembered) {
            val intentHome = Intent(this, MainActivity::class.java)
            startActivity(intentHome)
            finish()
        }

        _binding.register.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        _binding.buttonLogin.setOnClickListener {
            val email = _binding.editTextEmail.text.toString().trim()
            val password = _binding.editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                _binding.editTextEmail.error = "Email is required"
            } else if (password.isEmpty()) {
                _binding.editTextPassword.error = "Password is required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _binding.editTextEmail.error = "Email address is not valid"
            } else {
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean(REMEMBER_ME, checkbox.isChecked)
                    editor.apply()

                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Sign in success.", Toast.LENGTH_SHORT).show()

                    val intentHome = Intent(this, MainActivity::class.java)
                    startActivity(intentHome)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Sign in failed. Please check your username/email or password",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}