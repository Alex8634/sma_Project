package com.example.smamysuperalarm.activities

//import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smamysuperalarm.R
import com.example.smamysuperalarm.database.AppDatabase
import com.example.smamysuperalarm.model.User
import com.example.smamysuperalarm.repository.UserRepository
import kotlinx.coroutines.launch

class MainActivity1 : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var sleepHoursInput: EditText
    private lateinit var codeInput: EditText
    private lateinit var continueButton: Button
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        Log.d("MainActivity1", "onCreate started")

        // Initialize Room database and repository
        val database = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database.userDao())

        // Initialize views
        nameInput = findViewById(R.id.name_input)
        sleepHoursInput = findViewById(R.id.sleep_hours_input)
        codeInput = findViewById(R.id.work_type_input)
        continueButton = findViewById(R.id.button1)
        
        Log.d("MainActivity1", "Views initialized")

        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val hasSeenWelcome = prefs.getBoolean("hasSeenWelcome", false)
        //nu arata pagina 1 daca a fost vizitata deja
        /*if (hasSeenWelcome) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
            return
        }*/

        continueButton.setOnClickListener {
            Log.d("MainActivity1", "Button clicked")
            
            val username = nameInput.text.toString().trim()
            val sleepHoursStr = sleepHoursInput.text.toString().trim()
            val code = codeInput.text.toString().trim()
            
            Log.d("MainActivity1", "Username: $username, Sleep Hours: $sleepHoursStr, Code: $code")
            
            if (username.isEmpty()) {
                Log.d("MainActivity1", "Username is empty")
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (sleepHoursStr.isEmpty()) {
                Log.d("MainActivity1", "Sleep hours is empty")
                Toast.makeText(this, "Please enter your default sleep schedule", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sleepHours = sleepHoursStr.toIntOrNull()
            if (sleepHours == null || sleepHours < 1 || sleepHours > 24) {
                Log.d("MainActivity1", "Invalid sleep hours")
                Toast.makeText(this, "Please enter a valid sleep schedule (1-24 hours)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (code.isEmpty()) {
                Log.d("MainActivity1", "Code is empty")
                Toast.makeText(this, "Please enter your code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save user to Room database
            lifecycleScope.launch {
                try {
                    val user = User(username = username, password = code, sleepHours = sleepHours)
                    userRepository.insertUser(user)
                    
                    // Save to SharedPreferences for backward compatibility
                    prefs.edit().apply {
                        putString("alarm_code", code)
                        putString("username", username)
                        putInt("sleep_hours", sleepHours)
                        putBoolean("hasSeenWelcome", true)
                        apply()
                    }
                    
                    Toast.makeText(this@MainActivity1, "Profile saved successfully", Toast.LENGTH_SHORT).show()
                    
                    val intent = Intent(this@MainActivity1, MainActivity2::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Log.e("MainActivity1", "Error saving user: ${e.message}")
                    Toast.makeText(this@MainActivity1, "Error saving profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //enableEdgeToEdge()
        // setContentView(R.layout.activity_main)
        
        Log.d("MainActivity1", "onCreate completed")
    }
}
