package com.example.smamysuperalarm.activities

//import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //sheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        //dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        //dialog.setContentView(sheetLayoutBinding.root)
        setContentView(binding.root)

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
