package com.example.agro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // User is signed in
                // Start home activity
                startActivity(Intent(this, Home::class.java))
            } else {
                // No user is signed in
                // start login activity
                startActivity(Intent(this, MainActivity::class.java))
            }
            // close splash activity
            // close splash activity
            finish()
        }, 2000)

        val heading = findViewById<ImageView>(R.id.logo)

        heading.alpha = 0f
        heading.animate().alpha(1f).duration = 1000;
        heading.translationY = -150F
        heading.animate().alpha(1f).translationYBy(150F).duration = 1000
    }
}