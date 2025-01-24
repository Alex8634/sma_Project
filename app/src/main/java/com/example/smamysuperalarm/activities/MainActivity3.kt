package com.example.smamysuperalarm.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.data.PersonDatabase
import com.example.smamysuperalarm.databinding.ActivityMain3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding3: ActivityMain3Binding
    private lateinit var db: PersonDatabase
    private lateinit var hello: TextView
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding3= ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding3.root)

        db = PersonDatabase.getDb(this)
        hello = findViewById(R.id.wellhellothere)

        lifecycleScope.launch(Dispatchers.IO) {
            val pers = db.personDao().getPersonByName("Alex")
           /*
            val name = pers.nume

            withContext(Dispatchers.Main) {
                if (name.isNotEmpty()) {
                    hello.text = name
                } else {
                    hello.text = "error"
                }
            }
            */
        }

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