package com.example.smamysuperalarm.activities

//import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.databinding.ActivityMain1Binding

class MainActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityMain1Binding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        //binding = ActivityMain1Binding.inflate(layoutInflater)
        //sheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        //dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        //dialog.setContentView(sheetLayoutBinding.root)
        //setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val editTextUsername = findViewById<EditText>(R.id.name)
        val editTextPassword = findViewById<EditText>(R.id.code)
        val buttonLogin = findViewById<Button>(R.id.button1)

        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val hasSeenWelcome = prefs.getBoolean("hasSeenWelcome", false)
        //nu arata pagina 1 daca a fost vizitata deja
        /*if (hasSeenWelcome) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
            return
        }*/

        setContentView(R.layout.activity_main1)

        val continueButton: Button = findViewById(R.id.button1)
        continueButton.setOnClickListener {
            prefs.edit().putBoolean("hasSeenWelcome", true).apply()

            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }
        //enableEdgeToEdge()
        // setContentView(R.layout.activity_main)
        buttonLogin.setOnClickListener {

            val username = binding.name.text.toString().trim()
            val age = binding.age.text.toString().trim()
            val programme = binding.code.text.toString().trim()
            if (username.isNotEmpty() && age.isNotEmpty() && programme.isNotEmpty()) {
                val intent = Intent(this, MainActivity3::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
