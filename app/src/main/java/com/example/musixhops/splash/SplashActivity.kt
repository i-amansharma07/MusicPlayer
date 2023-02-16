package com.example.musixhops.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.musixhops.main.MainActivity
import com.example.musixhops.databinding.ActivitySplashBinding

private const val SPLASH_TIMER=3000L
class SplashActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(view.root)
        binding = view


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }, SPLASH_TIMER)
    }
}