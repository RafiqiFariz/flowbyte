package com.flowbyte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.flowbyte.ui.home.HomeFragment

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        val textViewSignUp = findViewById<TextView>(R.id.login)
        textViewSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerBtn = findViewById<Button>(R.id.buttonRegister)
        registerBtn.setOnClickListener{
            val intentHome = Intent(this, MainActivity::class.java)
            startActivity(intentHome)
        }
    }
}