package com.flowbyte

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        supportActionBar?.hide()
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreenActivity, GettingStartedActivity::class.java))
            finish()
        }, 2000)

    }
}