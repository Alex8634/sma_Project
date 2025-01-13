package com.example.smamysuperalarm

import android.content.Intent
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smamysuperalarm.databinding.ActivityMain2Binding
import com.example.smamysuperalarm.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding2: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding2.root)
        binding2.button2.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
    }
}