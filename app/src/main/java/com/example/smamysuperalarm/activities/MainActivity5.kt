package com.example.smamysuperalarm.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding5: ActivityMain5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding5 = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding5.root)

        binding5.done.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

    }
}