package com.example.smamysuperalarm.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain6Binding

class MainActivity6 : AppCompatActivity() {
    private val binding6: ActivityMain6Binding by lazy{
        ActivityMain6Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding6.root)

        //normal alarm button
        val continueButton: Button = findViewById(R.id.add_alarm)
        continueButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //back to menu button
        val backButton: Button = findViewById(R.id.back6)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        }
        //SuperAlarm Button
        val continueButtonSalarm: Button = findViewById(R.id.add_Salarm)
        continueButtonSalarm.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            startActivity(intent)
            finish()
        }
    }
}