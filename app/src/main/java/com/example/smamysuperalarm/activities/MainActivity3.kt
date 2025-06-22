package com.example.smamysuperalarm.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding3: ActivityMain3Binding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding3 = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding3.root)

        // Get the saved username
        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "User") ?: "User"
        
        // Update the greeting text
        binding3.greeting.text = "Hello, $username!"

        binding3.preferencesButton.setOnClickListener {
            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }
        
        binding3.config.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
    }
}