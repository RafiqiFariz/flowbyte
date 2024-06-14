package com.flowbyte

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class GettingStartedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gettingstarted)
        supportActionBar?.hide()

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
//            val intentLogin = Intent(this, LoginActivity::class.java)
            val intentLogin = Intent(this, MainActivity::class.java) // utk bypass
            startActivity(intentLogin)
        }
    }
}