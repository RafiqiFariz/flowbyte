package com.flowbyte

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.flowbyte.databinding.ActivitySignUpBinding
import com.flowbyte.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        // go to login activity
        val loginLink = findViewById<TextView>(R.id.login)
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Register Button can't be clicked if there is an empty input field or email patter is not right
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
//        val editTextUsername = findViewById<TextView>(R.id.editTextUsername)
        val editTextEmail = findViewById<TextView>(R.id.editTextEmail)
        val editTextPassword = findViewById<TextView>(R.id.editTextPassword)
        buttonRegister.setOnClickListener{
//            val username = editTextUsername.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isNotEmpty() || password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "Authentication success. please login",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val intentLogin = Intent(this, LoginActivity::class.java)
                            startActivity(intentLogin)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failed")
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "All field must be filled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}