package com.example.smamysuperalarm.activities

//import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain1Binding

class MainActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityMain1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain1Binding.inflate(layoutInflater)
        //sheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        //dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        //dialog.setContentView(sheetLayoutBinding.root)
        setContentView(binding.root)

        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val hasSeenWelcome = prefs.getBoolean("hasSeenWelcome", false)

        if (hasSeenWelcome) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main1)

        val continueButton: Button = findViewById(R.id.button1)
        continueButton.setOnClickListener {
            prefs.edit().putBoolean("hasSeenWelcome", true).apply()

            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }
        //enableEdgeToEdge()
        // setContentView(R.layout.activity_main)
        binding.button1.setOnClickListener {

            val username = binding.nameInput.text.toString().trim()
            val age = binding.ageInput.text.toString().trim()
            val programme = binding.workTypeInput.text.toString().trim()
            if (username.isNotEmpty() && age.isNotEmpty() && programme.isNotEmpty()) {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
