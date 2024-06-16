package com.flowbyte.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.flowbyte.R

class GettingStartedActivity : AppCompatActivity() {
    private val REMEMBER_ME: String = "rememberMe"
    private val SHARED_PREFS: String = "sharedPrefs"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gettingstarted)
        supportActionBar?.hide()

        // Check if user has already logged in and checked the checkbox
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val isRemembered = sharedPreferences.getBoolean(REMEMBER_ME, false)

        if (isRemembered) {
            // User has logged in and checked the checkbox, navigate to MainActivity
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
            finish()
            return  // Exit onCreate method to prevent further execution
        }

        val text =
            "Explore unlimited music with our app, accessing your favorite songs and playlists"

        // Define words to highlight
        val highlightWords = listOf("unlimited music", "favorite songs")

        // Create a SpannableString from the text
        val spannableString = SpannableString(text)

        // Get the color from resources
        val highlightColor = ContextCompat.getColor(this, R.color.light_sea_green)

        // Apply highlight to each word
        for (word in highlightWords) {
            val startIndex = text.indexOf(word)
            if (startIndex >= 0) {
                val endIndex = startIndex + word.length
                spannableString.setSpan(
                    ForegroundColorSpan(highlightColor),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        // Set the spannable string to the TextView
        val desc = findViewById<TextView>(R.id.desc)
        desc.text = spannableString

        val btnGettingStart = findViewById<Button>(R.id.buttonGettingStarted)
        btnGettingStart.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
//            val intentLogin = Intent(this, MainActivity::class.java) // utk bypass
            startActivity(intentLogin)
        }
    }
}